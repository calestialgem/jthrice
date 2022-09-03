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
    var generator = new Generator(resolution, build, entity.get());
    generator.generate();
  }

  /** Resolution of the generated program entity. */
  private final Resolution     resolution;
  /** Path to the build folder. */
  private final Path           build;
  /** Generated program entity. */
  private final Entity.Program entity;
  /** Buffer to append the code into. */
  private final StringBuilder  buffer;
  /** Current level of indentation. */
  private int                  indentation;

  /** Object that represents a new line. */
  private static final class NewLine {
    /** Identation level of the new line. */
    public final int indentation;

    public NewLine(int indentation) {
      this.indentation = indentation;
    }
  }

  private NewLine newLine() {
    return new NewLine(this.indentation);
  }

  private Generator(Resolution resolution, Path build, Entity.Program entity) {
    this.resolution  = resolution;
    this.build       = build;
    this.entity      = entity;
    this.buffer      = new StringBuilder();
    this.indentation = 0;
  }

  /** Generate the root entity. */
  private void generate() {
    this.generate("""
                  #include <stdio.h>
                  #include <stdint.h>

                  int main (int argc, char** argv) {
                  """);
    this.indentation = 1;
    this.entity.statements.forEach(this::generate);
    this.indentation = 0;
    this.generate("}");
    var compilerFlags = new CompilerFlags(this.resolution, Generator.COMPILER,
      this.build);
    compilerFlags.write(this.buffer.toString());
    compilerFlags.compile();
  }

  /** Generate the given objects in order. */
  private void generate(Object... objects) {
    for (var object : objects) {
      switch (object) {
        case Entity.Statement statement -> this.generateStatement(statement);
        case Entity.Expression expression ->
          this.generateExpression(expression);
        case Type type -> this.generateType(type);
        case Name name -> this.generateName(name);
        case String string -> this.generateString(string);
        case NewLine newLine -> this.generateNewLine(newLine);
        default -> Bug.unreachable("Object is not generatable!");
      }
    }
  }

  /** Generate the given statement. */
  private void generateStatement(Entity.Statement statement) {
    switch (statement) {
      case Entity.Statement.Definition definition ->
        this.generateDefinition(definition);
    }
    this.generate(this.newLine());
  }

  /** Generate the given definition. */
  private void generateDefinition(Entity.Statement.Definition definition) {

    this.generate(definition.type, definition.name, "=", definition.value, ";");
    switch (definition.type) {
      case Type.Scalar scalar -> this.generatePrint(scalar, definition.name);
      default -> this.generate("");
    }
  }

  /** Generate the print of the given scalar. */
  private void generatePrint(Type.Scalar scalar, Name name) {
    this.generate(this.newLine(), "printf(\"", name, " = %");
    switch (scalar) {
      case Type.Scalar.Signed signed -> this.generate(switch (signed) {
        case Type.Scalar.Signed.I1 i1 -> "hhi";
        case Type.Scalar.Signed.I2 i2 -> "hi";
        case Type.Scalar.Signed.I4 i4 -> "i";
        case Type.Scalar.Signed.I8 i8 -> "lli";
        case Type.Scalar.Signed.IX ix -> "lli";
      });
      case Type.Scalar.Unsigned unsigned -> this.generate(switch (unsigned) {
        case Type.Scalar.Unsigned.U1 u1 -> "hhu";
        case Type.Scalar.Unsigned.U2 u2 -> "hu";
        case Type.Scalar.Unsigned.U4 u4 -> "u";
        case Type.Scalar.Unsigned.U8 u8 -> "llu";
        case Type.Scalar.Unsigned.UX ux -> "llu";
      });
      case Type.Scalar.Floating floating -> this.generate("f");
    }
    this.generate("\n\");", this.newLine());
  }

  /** Generate the given type. */
  private void generateType(Type type) {
    switch (type) {
      case Type.Scalar scalar -> this.generateScalar(scalar);
      case Type.Meta meta -> Bug.unreachable("Cannot generate a meta type!");
    }
  }

  /** Generate the given scalar type. */
  private void generateScalar(Type.Scalar scalar) {
    switch (scalar) {
      case Type.Scalar.Signed signed -> this.generateSigned(signed);
      case Type.Scalar.Unsigned unsigned -> this.generateUnsigned(unsigned);
      case Type.Scalar.Floating floating -> this.generateFloating(floating);
    }
  }

  /** Generate the given signed scalar type. */
  private void generateSigned(Type.Scalar.Signed signed) {
    switch (signed) {
      case Type.Scalar.Signed.I1 i1 -> this.generateString("int8_t");
      case Type.Scalar.Signed.I2 i2 -> this.generateString("int16_t");
      case Type.Scalar.Signed.I4 i4 -> this.generateString("int32_t");
      case Type.Scalar.Signed.I8 i8 -> this.generateString("int64_t");
      case Type.Scalar.Signed.IX ix -> this.generateString("intptr_t");
    }
  }

  /** Generate the given unsigned scalar type. */
  private void generateUnsigned(Type.Scalar.Unsigned unsigned) {
    switch (unsigned) {
      case Type.Scalar.Unsigned.U1 u1 -> this.generateString("uint8_t");
      case Type.Scalar.Unsigned.U2 u2 -> this.generateString("uint16_t");
      case Type.Scalar.Unsigned.U4 u4 -> this.generateString("uint32_t");
      case Type.Scalar.Unsigned.U8 u8 -> this.generateString("uint64_t");
      case Type.Scalar.Unsigned.UX ux -> this.generateString("uintptr_t");
    }
  }

  /** Generate the given floating scalar type. */
  private void generateFloating(Type.Scalar.Floating floating) {
    switch (floating) {
      case Type.Scalar.Floating.F4 f4 -> this.generateString("float");
      case Type.Scalar.Floating.F8 f8 -> this.generateString("double");
    }
  }

  /** Generate the given name. */
  private void generateName(Name name) {
    this.generate(name.value);
  }

  /** Generate the given expression. */
  private void generateExpression(Entity.Expression expression) {
    switch (expression) {
      case Entity.Expression.Primary primary -> this.generatePrimary(primary);
      case Entity.Expression.Unary unary -> this.generateUnary(unary);
      case Entity.Expression.Binary binary -> this.generateBinary(binary);
    }
  }

  /** Generate the given primary expression. */
  private void generatePrimary(Entity.Expression.Primary primary) {
    switch (primary) {
      case Entity.Expression.Primary.Literal literal ->
        this.generateLiteral(literal);
      case Entity.Expression.Primary.Access access ->
        this.generateAccess(access);
    }
  }

  /** Generate the given literal primary expression. */
  private void generateLiteral(Entity.Expression.Primary.Literal literal) {
    this.generate(literal.value.toString());
  }

  /** Generate the given access primary expression. */
  private void generateAccess(Entity.Expression.Primary.Access access) {
    this.generate(access.name);
  }

  /** Generate the given unary expression. */
  private void generateUnary(Entity.Expression.Unary unary) {
    this.generate(unary.operator, unary.operand);
  }

  /** Generate the given binary expression. */
  private void generateBinary(Entity.Expression.Binary binary) {
    this.generate(binary.left, binary.operator, binary.right);
  }

  /** Generate the given string. */
  private void generateString(String string) {
    this.buffer.append(string);
  }

  /** Generate the given new line. */
  private void generateNewLine(NewLine newLine) {
    final var INDENTATION = "    ";
    this.buffer.append(System.lineSeparator());
    for (var i = 0; i < newLine.indentation; i++) {
      this.buffer.append(INDENTATION);
    }
  }
}
