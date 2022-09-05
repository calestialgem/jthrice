// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

import java.nio.file.*;

import jthrice.analyzer.*;
import jthrice.launcher.*;
import jthrice.resolver.*;

/** Writes and compiles a C source from a program entity. */
public class Generator {
  /** C compiler. */
  public static final String COMPILER = "clang";

  /** Generate the C source from the given entity to the given build-path and
   * report to the given resolution. */
  public static void generate(Resolution resolution, Path build,
    Entity.Program entity) {
    var buffer = new StringBuilder();
    Generator.generateIncludes(buffer, "stdio", "stdint");
    Generator.generate(buffer, buffer, Indentation.of(0),
      "int main(int argc, char** argv) {");
    for (var statement : entity.statements) {
      Generator.generateStatement(buffer, statement, Indentation.of(1));
    }
    Generator.generate(buffer, buffer, Indentation.of(0), "}");
    var compilerFlags = new CompilerFlags(resolution, Generator.COMPILER,
      build);
    compilerFlags.write(buffer.toString());
    compilerFlags.compile();
  }

  /** Generate the given objects in order to the given buffer. */
  private static void generate(StringBuilder buffer, Object... objects) {
    for (var object : objects) {
      switch (object) {
        case Entity.Expression expression ->
          Generator.generateExpression(buffer, expression);
        case Type type -> Generator.generateType(buffer, type);
        case Indentation indentation ->
          Generator.generateNewLine(buffer, indentation);
        default -> buffer.append(object);
      }
    }
  }

  /** Generate the includes for the given header files to the given buffer. */
  private static void generateIncludes(StringBuilder buffer,
    String... headers) {
    for (var header : headers) {
      Generator.generate(buffer, "#include <", header, ".h>\n");
    }
  }

  /** Generate the given statement entity to the given buffer with the given
   * indentation. */
  private static void generateStatement(StringBuilder buffer,
    Entity.Statement statement, Indentation indentation) {
    switch (statement) {
      case Entity.Definition definition ->
        Generator.generateDefinition(buffer, definition, indentation);
    }
    Generator.generate(buffer, indentation);
  }

  /** Generate the given definition entity to the given buffer with the given
   * indentation. */
  private static void generateDefinition(StringBuilder buffer,
    Entity.Definition definition, Indentation indentation) {

    Generator.generate(buffer, definition.type, definition.name, "=",
      definition.value, ";");
    switch (definition.type) {
      case Type.Scalar scalar -> Generator.generatePrint(buffer, scalar,
        definition.name.value, indentation);
      default -> Generator.generate(buffer, "");
    }
  }

  /** Generate the print of the given scalar entity with the given name to the
   * given buffer with the given indentation. */
  private static void generatePrint(StringBuilder buffer, Type.Scalar scalar,
    String name, Indentation indentation) {
    Generator.generate(buffer, indentation, "printf(\"", name, " = %");
    switch (scalar) {
      case Type.Signed signed -> Generator.generate(buffer, switch (signed) {
        case Type.I1 i1 -> "hhi";
        case Type.I2 i2 -> "hi";
        case Type.I4 i4 -> "i";
        case Type.I8 i8 -> "lli";
        case Type.Ix ix -> "lli";
      });
      case Type.Unsigned unsigned ->
        Generator.generate(buffer, switch (unsigned) {
          case Type.U1 u1 -> "hhu";
          case Type.U2 u2 -> "hu";
          case Type.U4 u4 -> "u";
          case Type.U8 u8 -> "llu";
          case Type.Ux ux -> "llu";
        });
      case Type.Floating floating -> Generator.generate(buffer, "f");
      case Type.Rinf rinf ->
        throw new RuntimeException("There is an undeduced `rinf` type!");
    }
    Generator.generate(buffer, "\n\");", indentation);
  }

  /** Generate the given type to the given buffer. */
  private static void generateType(StringBuilder buffer, Type type) {
    switch (type) {
      case Type.Scalar scalar -> Generator.generateScalar(buffer, scalar);
      case Type.Meta meta ->
        throw new RuntimeException("Cannot generate a meta type!");
    }
  }

  /** Generate the given scalar type to the given buffer. */
  private static void generateScalar(StringBuilder buffer, Type.Scalar scalar) {
    switch (scalar) {
      case Type.Signed signed -> Generator.generateSigned(buffer, signed);
      case Type.Unsigned unsigned ->
        Generator.generateUnsigned(buffer, unsigned);
      case Type.Floating floating ->
        Generator.generateFloating(buffer, floating);
      case Type.Rinf ring ->
        throw new RuntimeException("There is an undeduced `rinf` type!");
    }
  }

  /** Generate the given signed scalar type to the given buffer. */
  private static void generateSigned(StringBuilder buffer, Type.Signed signed) {
    switch (signed) {
      case Type.I1 i1 -> Generator.generate(buffer, "int8_t");
      case Type.I2 i2 -> Generator.generate(buffer, "int16_t");
      case Type.I4 i4 -> Generator.generate(buffer, "int32_t");
      case Type.I8 i8 -> Generator.generate(buffer, "int64_t");
      case Type.Ix ix -> Generator.generate(buffer, "intptr_t");
    }
  }

  /** Generate the given unsigned scalar type to the given buffer. */
  private static void generateUnsigned(StringBuilder buffer,
    Type.Unsigned unsigned) {
    switch (unsigned) {
      case Type.U1 u1 -> Generator.generate(buffer, "uint8_t");
      case Type.U2 u2 -> Generator.generate(buffer, "uint16_t");
      case Type.U4 u4 -> Generator.generate(buffer, "uint32_t");
      case Type.U8 u8 -> Generator.generate(buffer, "uint64_t");
      case Type.Ux ux -> Generator.generate(buffer, "uintptr_t");
    }
  }

  /** Generate the given floating scalar type to the given buffer. */
  private static void generateFloating(StringBuilder buffer,
    Type.Floating floating) {
    switch (floating) {
      case Type.F4 f4 -> Generator.generate(buffer, "float");
      case Type.F8 f8 -> Generator.generate(buffer, "double");
    }
  }

  /** Generate the given expression entity to the given buffer. */
  private static void generateExpression(StringBuilder buffer,
    Entity.Expression expression) {
    switch (expression) {
      case Entity.Literal literal -> Generator.generateLiteral(buffer, literal);
      case Entity.Access access -> Generator.generateAccess(buffer, access);
      case Entity.Unary unary -> Generator.generateUnary(buffer, unary);
      case Entity.Binary binary -> Generator.generateBinary(buffer, binary);
    }
  }

  /** Generate the given literal entity to the given buffer. */
  private static void generateLiteral(StringBuilder buffer,
    Entity.Literal literal) {
    Generator.generate(buffer, literal.value.toString());
  }

  /** Generate the given access entity to the given buffer. */
  private static void generateAccess(StringBuilder buffer,
    Entity.Access access) {
    Generator.generate(buffer, access.variable);
  }

  /** Generate the given unary entity to the given buffer. */
  private static void generateUnary(StringBuilder buffer, Entity.Unary unary) {
    Generator.generate(buffer, unary.operator, unary.operand);
  }

  /** Generate the given binary entity to the given buffer. */
  private static void generateBinary(StringBuilder buffer,
    Entity.Binary binary) {
    Generator.generate(buffer, binary.left, binary.operator, binary.right);
  }

  /** Generate a new line with the given indentation to the given buffer. */
  private static void generateNewLine(StringBuilder buffer,
    Indentation indentation) {
    final var INDENTATION = "  ";
    buffer.append(System.lineSeparator());
    for (var i = 0; i < indentation.level; i++) {
      buffer.append(INDENTATION);
    }
  }
}
