// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

import java.math.*;
import java.nio.file.*;
import java.util.*;

import jthrice.analyzer.*;
import jthrice.launcher.*;

/** Writes and compiles a C source from a solution. */
public final class Generator {
  /** C compiler. */
  public static final String COMPILER = "clang";

  /** Generate the C source from the given solution to the given build-path and
   * report to the given resolution. */
  public static void generate(Resolution resolution, Path build,
    Solution solution) {
    var buffer = new StringBuilder();
    Generator.generateIncludes(buffer, "stdio", "stdint");
    Generator.generate(buffer, Indentation.of(0),
      "int main(int argc, char** argv) {", Indentation.of(1));
    for (var symbol : solution.symbols.values()) {
      Generator.generateSymbol(buffer, symbol, Indentation.of(1));
    }
    Generator.generate(buffer, Indentation.of(0), "}");
    var compilerFlags = new CompilerFlags(resolution, Generator.COMPILER,
      build);
    compilerFlags.write(buffer.toString());
    compilerFlags.compile();
  }

  /** Generate the given objects in order to the given buffer. */
  private static void generate(StringBuilder buffer, Object... objects) {
    for (var object : objects) {
      switch (object) {
        case Evaluation evaluation ->
          Generator.generateEvaluation(buffer, evaluation);
        case Type type -> Generator.generateType(buffer, type);
        case Indentation indentation ->
          Generator.generateNewLine(buffer, indentation);
        case List<?> list ->
          list.forEach(obj -> Generator.generate(buffer, obj));
        case BigDecimal decimal -> buffer.append(decimal.toPlainString());
        case StringBuilder builder ->
          throw new RuntimeException("Try to generate the buffer!");
        default -> buffer.append(object);
      }
    }
  }

  /** Generate the includes for the given header files to the given buffer. */
  private static void generateIncludes(StringBuilder buffer,
    String... headers) {
    for (var header : headers) {
      Generator.generate(buffer, "#include <", header, ".h>",
        Indentation.of(0));
    }
  }

  /** Generate the given symbol to the given buffer with the given
   * indentation. */
  private static void generateSymbol(StringBuilder buffer, Symbol symbol,
    Indentation indentation) {
    switch (symbol) {
      case TypeSymbol typeSymbol ->
        Generator.generateTypeSymbol(buffer, typeSymbol, indentation);
      case Variable variable ->
        Generator.generateVariable(buffer, variable, indentation);
    }
  }

  /** Generate the given type symbol to the given buffer with the given
   * indentation. */
  private static void generateTypeSymbol(StringBuilder buffer,
    TypeSymbol typeSymbol, Indentation indentation) {
  }

  /** Generate the given variable to the given buffer with the given
   * indentation. */
  private static void generateVariable(StringBuilder buffer, Variable variable,
    Indentation indentation) {

    Generator.generate(buffer, variable.evaluation.type, " ", variable.name,
      " = ", variable.evaluation, ";", indentation);

    switch (variable.evaluation.type) {
      case Scalar scalar ->
        Generator.generatePrint(buffer, scalar, variable.name, indentation);
      default -> Generator.generate(buffer, "");
    }
    Generator.generate(buffer, indentation);
  }

  /** Generate the print of the given scalar with the given name to the given
   * buffer with the given indentation. */
  private static void generatePrint(StringBuilder buffer, Scalar scalar,
    String name, Indentation indentation) {
    Generator.generate(buffer, "printf(\"", name, " = %");
    switch (scalar) {
      case Scalar.Signed signed -> Generator.generate(buffer, switch (signed) {
        case Scalar.I1 i1 -> "hhi";
        case Scalar.I2 i2 -> "hi";
        case Scalar.I4 i4 -> "i";
        case Scalar.I8 i8 -> "lli";
        case Scalar.Ix ix -> "lli";
      });
      case Scalar.Unsigned unsigned ->
        Generator.generate(buffer, switch (unsigned) {
          case Scalar.U1 u1 -> "hhu";
          case Scalar.U2 u2 -> "hu";
          case Scalar.U4 u4 -> "u";
          case Scalar.U8 u8 -> "llu";
          case Scalar.Ux ux -> "llu";
        });
      case Scalar.Floating floating -> Generator.generate(buffer, "f");
      case Scalar.Rinf rinf ->
        throw new RuntimeException("There is an undeduced `rinf` type!");
    }
    Generator.generate(buffer, "\\n\", ", name, ");", indentation);
  }

  /** Generate the given type to the given buffer. */
  private static void generateType(StringBuilder buffer, Type type) {
    switch (type) {
      case Scalar scalar -> Generator.generateScalar(buffer, scalar);
      case Type.Meta meta ->
        throw new RuntimeException("Cannot generate a meta type!");
    }
  }

  /** Generate the given scalar type to the given buffer. */
  private static void generateScalar(StringBuilder buffer, Scalar scalar) {
    switch (scalar) {
      case Scalar.Signed signed -> Generator.generateSigned(buffer, signed);
      case Scalar.Unsigned unsigned ->
        Generator.generateUnsigned(buffer, unsigned);
      case Scalar.Floating floating ->
        Generator.generateFloating(buffer, floating);
      case Scalar.Rinf ring ->
        throw new RuntimeException("There is an undeduced `rinf` type!");
    }
  }

  /** Generate the given signed scalar type to the given buffer. */
  private static void generateSigned(StringBuilder buffer,
    Scalar.Signed signed) {
    switch (signed) {
      case Scalar.I1 i1 -> Generator.generate(buffer, "int8_t");
      case Scalar.I2 i2 -> Generator.generate(buffer, "int16_t");
      case Scalar.I4 i4 -> Generator.generate(buffer, "int32_t");
      case Scalar.I8 i8 -> Generator.generate(buffer, "int64_t");
      case Scalar.Ix ix -> Generator.generate(buffer, "intptr_t");
    }
  }

  /** Generate the given unsigned scalar type to the given buffer. */
  private static void generateUnsigned(StringBuilder buffer,
    Scalar.Unsigned unsigned) {
    switch (unsigned) {
      case Scalar.U1 u1 -> Generator.generate(buffer, "uint8_t");
      case Scalar.U2 u2 -> Generator.generate(buffer, "uint16_t");
      case Scalar.U4 u4 -> Generator.generate(buffer, "uint32_t");
      case Scalar.U8 u8 -> Generator.generate(buffer, "uint64_t");
      case Scalar.Ux ux -> Generator.generate(buffer, "uintptr_t");
    }
  }

  /** Generate the given floating scalar type to the given buffer. */
  private static void generateFloating(StringBuilder buffer,
    Scalar.Floating floating) {
    switch (floating) {
      case Scalar.F4 f4 -> Generator.generate(buffer, "float");
      case Scalar.F8 f8 -> Generator.generate(buffer, "double");
    }
  }

  /** Generate the given evaluation to the given buffer. */
  private static void generateEvaluation(StringBuilder buffer,
    Evaluation evaluation) {
    switch (evaluation) {
      case Evaluation.Nofix nofix -> Generator.generateNofix(buffer, nofix);
      case Evaluation.Prefix prefix -> Generator.generatePrefix(buffer, prefix);
      case Evaluation.Postfix postfix ->
        Generator.generatePostfix(buffer, postfix);
      case Evaluation.Infix infix -> Generator.generateInfix(buffer, infix);
      case Evaluation.Outfix outfix -> Generator.generateOutfix(buffer, outfix);
      case Evaluation.Knitfix knitfix ->
        Generator.generateKnitfix(buffer, knitfix);
    }
  }

  /** Generate the given nofix to the given buffer. */
  private static void generateNofix(StringBuilder buffer,
    Evaluation.Nofix nofix) {
    if (nofix.known()) {
      Generator.generate(buffer, nofix.value);
      return;
    }
    Generator.generate(buffer, nofix.first);
  }

  /** Generate the given prefix to the given buffer. */
  private static void generatePrefix(StringBuilder buffer,
    Evaluation.Prefix prefix) {
    if (prefix.known()) {
      Generator.generate(buffer, prefix.value);
      return;
    }
    Generator.generate(buffer, prefix.before, prefix.last);
  }

  /** Generate the given postfix to the given buffer. */
  private static void generatePostfix(StringBuilder buffer,
    Evaluation.Postfix postfix) {
    if (postfix.known()) {
      Generator.generate(buffer, postfix.value);
      return;
    }
    Generator.generate(buffer, postfix.first, postfix.after);
  }

  /** Generate the given infix to the given buffer. */
  private static void generateInfix(StringBuilder buffer,
    Evaluation.Infix infix) {
    if (infix.known()) {
      Generator.generate(buffer, infix.value);
      return;
    }
    Generator.generate(buffer, infix.first, infix.between, infix.last);
  }

  /** Generate the given outfix to the given buffer. */
  private static void generateOutfix(StringBuilder buffer,
    Evaluation.Outfix outfix) {
    if (outfix.known()) {
      Generator.generate(buffer, outfix.value);
      return;
    }
    Generator.generate(buffer, outfix.before, outfix.middle, outfix.after);
  }

  /** Generate the given knitfix to the given buffer. */
  private static void generateKnitfix(StringBuilder buffer,
    Evaluation.Knitfix knitfix) {
    if (knitfix.known()) {
      Generator.generate(buffer, knitfix.value);
      return;
    }
    Generator.generate(buffer, knitfix.before, knitfix.between, knitfix.after,
      knitfix.first, knitfix.middle, knitfix.last);
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
