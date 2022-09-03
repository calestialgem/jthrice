// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

import java.nio.file.Path;

import jthrice.analyzer.Analyzer;
import jthrice.analyzer.Entity;
import jthrice.analyzer.Name;
import jthrice.analyzer.Type;
import jthrice.launcher.Resolution;
import jthrice.utility.Bug;

/** Writes and compiles a C source from a program entity. */
public class Generator {
    /** C compiler. */
    public static final String COMPILER = "clang";

    /** Generate the C source from the source in the given resolution. */
    public static void generate(Resolution resolution, Path build) {
        var entity = Analyzer.analyze(resolution);
        if (entity.empty()) {
            resolution.warning("GENERATOR", "Not continuing due to errors.");
            return;
        }
        Generator generator = new Generator(resolution, build, entity.get());
        generator.generate();
    }

    /** Resolution of the generated program entity. */
    private final Resolution resolution;
    /** Path to the build folder. */
    private final Path build;
    /** Generated program entity. */
    private final Entity.Program entity;
    /** Buffer to append the code into. */
    private final StringBuilder buffer;
    /** Current level of indentation. */
    private int indentation;

    /** Object that represents a new line. */
    private final class NewLine {
        /** Identation level of the new line. */
        public final int indentation;

        public NewLine(int indentation) {
            this.indentation = indentation;
        }
    }

    private NewLine newLine() {
        return new NewLine(indentation);
    }

    private Generator(Resolution resolution, Path build, Entity.Program entity) {
        this.resolution = resolution;
        this.build = build;
        this.entity = entity;
        buffer = new StringBuilder();
        indentation = 0;
    }

    /** Generate the root entity. */
    private void generate() {
        generate("""
                #include <stdio.h>
                #include <stdint.h>

                int main (int argc, char** argv) {
                """);
        indentation = 1;
        entity.statements.forEach(statement -> generate(statement));
        indentation = 0;
        generate("}");
        CompilerFlags compilerFlags = new CompilerFlags(resolution, COMPILER, build);
        compilerFlags.write(buffer.toString());
        compilerFlags.compile();
    }

    /** Generate the given objects in order. */
    private void generate(Object... objects) {
        for (var object : objects) {
            switch (object) {
                case Entity.Statement statement -> generateStatement(statement);
                case Entity.Expression expression -> generateExpression(expression);
                case Type type -> generateType(type);
                case Name name -> generateName(name);
                case String string -> generateString(string);
                case NewLine newLine -> generateNewLine(newLine);
                default -> Bug.unreachable("Object is not generatable!");
            }
        }
    }

    /** Generate the given statement. */
    private void generateStatement(Entity.Statement statement) {
        switch (statement) {
            case Entity.Statement.Definition definition -> generateDefinition(definition);
        }
        generate(newLine());
    }

    /** Generate the given definition. */
    private void generateDefinition(Entity.Statement.Definition definition) {

        generate(definition.type, definition.name, "=", definition.value, ";");
        switch (definition.type) {
            case Type.Scalar scalar -> generatePrint(scalar, definition.name);
            default -> generate("");
        }
    }

    /** Generate the print of the given scalar. */
    private void generatePrint(Type.Scalar scalar, Name name) {
        generate(newLine(), "printf(\"",
                name, " = %");
        switch (scalar) {
            case Type.Scalar.Signed signed -> generate(switch (signed) {
                case Type.Scalar.Signed.I1 i1 -> "hhi";
                case Type.Scalar.Signed.I2 i2 -> "hi";
                case Type.Scalar.Signed.I4 i4 -> "i";
                case Type.Scalar.Signed.I8 i8 -> "lli";
                case Type.Scalar.Signed.IX ix -> "lli";
            });
            case Type.Scalar.Unsigned unsigned -> generate(switch (unsigned) {
                case Type.Scalar.Unsigned.U1 u1 -> "hhu";
                case Type.Scalar.Unsigned.U2 u2 -> "hu";
                case Type.Scalar.Unsigned.U4 u4 -> "u";
                case Type.Scalar.Unsigned.U8 u8 -> "llu";
                case Type.Scalar.Unsigned.UX ux -> "llu";
            });
            case Type.Scalar.Floating floating -> generate("f");
        }
        generate("\n\");", newLine());
    }

    /** Generate the given type. */
    private void generateType(Type type) {
        switch (type) {
            case Type.Scalar scalar -> generateScalar(scalar);
            case Type.Meta meta -> generateMeta(meta);
        }
    }

    /** Generate the given scalar type. */
    private void generateScalar(Type.Scalar scalar) {
        switch (scalar) {
            case Type.Scalar.Signed signed -> generateSigned(signed);
            case Type.Scalar.Unsigned unsigned -> generateUnsigned(unsigned);
            case Type.Scalar.Floating floating -> generateFloating(floating);
        }
    }

    /** Generate the given signed scalar type. */
    private void generateSigned(Type.Scalar.Signed signed) {
        switch (signed) {
            case Type.Scalar.Signed.I1 i1 -> generateString("int8_t");
            case Type.Scalar.Signed.I2 i2 -> generateString("int16_t");
            case Type.Scalar.Signed.I4 i4 -> generateString("int32_t");
            case Type.Scalar.Signed.I8 i8 -> generateString("int64_t");
            case Type.Scalar.Signed.IX ix -> generateString("intptr_t");
        }
    }

    /** Generate the given unsigned scalar type. */
    private void generateUnsigned(Type.Scalar.Unsigned unsigned) {
        switch (unsigned) {
            case Type.Scalar.Unsigned.U1 u1 -> generateString("uint8_t");
            case Type.Scalar.Unsigned.U2 u2 -> generateString("uint16_t");
            case Type.Scalar.Unsigned.U4 u4 -> generateString("uint32_t");
            case Type.Scalar.Unsigned.U8 u8 -> generateString("uint64_t");
            case Type.Scalar.Unsigned.UX ux -> generateString("uintptr_t");
        }
    }

    /** Generate the given floating scalar type. */
    private void generateFloating(Type.Scalar.Floating floating) {
        switch (floating) {
            case Type.Scalar.Floating.F4 f4 -> generateString("float");
            case Type.Scalar.Floating.F8 f8 -> generateString("double");
        }
    }

    /** Generate the given type type. */
    private void generateMeta(Type.Meta type) {
        Bug.unreachable("Cannot generate a meta type!");
    }

    /** Generate the given name. */
    private void generateName(Name name) {
        generate(name.value);
    }

    /** Generate the given expression. */
    private void generateExpression(Entity.Expression expression) {
        switch (expression) {
            case Entity.Expression.Primary primary -> generatePrimary(primary);
            case Entity.Expression.Unary unary -> generateUnary(unary);
            case Entity.Expression.Binary binary -> generateBinary(binary);
        }
    }

    /** Generate the given primary expression. */
    private void generatePrimary(Entity.Expression.Primary primary) {
        switch (primary) {
            case Entity.Expression.Primary.Literal literal -> generateLiteral(literal);
            case Entity.Expression.Primary.Access access -> generateAccess(access);
        }
    }

    /** Generate the given literal primary expression. */
    private void generateLiteral(Entity.Expression.Primary.Literal literal) {
        generate(literal.value.toString());
    }

    /** Generate the given access primary expression. */
    private void generateAccess(Entity.Expression.Primary.Access access) {
        generate(access.name);
    }

    /** Generate the given unary expression. */
    private void generateUnary(Entity.Expression.Unary unary) {
        generate(unary.operator, unary.operand);
    }

    /** Generate the given binary expression. */
    private void generateBinary(Entity.Expression.Binary binary) {
        generate(binary.left, binary.operator, binary.right);
    }

    /** Generate the given string. */
    private void generateString(String string) {
        buffer.append(string);
    }

    /** Generate the given new line. */
    private void generateNewLine(NewLine newLine) {
        final var INDENTATION = "    ";
        buffer.append(System.lineSeparator());
        for (var i = 0; i < newLine.indentation; i++) {
            buffer.append(INDENTATION);
        }
    }
}
