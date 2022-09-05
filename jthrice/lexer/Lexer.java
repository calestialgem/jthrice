// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.*;
import java.util.*;

import jthrice.launcher.*;

/** Groups the characters in a source file to a list of lexemes. */
public final class Lexer {
  /** Lex the source in the given resolution. */
  public static List<Lexeme> lex(Resolution resolution) {
    var   lexemes = new ArrayList<Lexeme>();
    Chunk unknown = null;

    for (var index = 0; resolution.source.exists(index); index++) {
      if (Lexer.isWhitespace(resolution.source.at(index))) {
        unknown = Lexer.report(resolution, unknown);
        continue;
      }

      var lexeme = Lexer.lexToken(resolution.source, index);
      if (lexeme == null) {
        lexeme = Lexer.lexNumber(resolution.source, index);
        if (lexeme == null) {
          lexeme = Lexer.lexWord(resolution.source, index);
        }
      }

      if (lexeme != null) {
        unknown = Lexer.report(resolution, unknown);
        lexemes.add(lexeme);
        index = lexeme.portion.last.index;
        continue;
      }

      if (unknown == null) {
        unknown = Chunk.of(resolution.source, index);
      }
      unknown.consume();
    }
    return lexemes;
  }

  /** Whether the given character is a whitespace that should be skipped. */
  private static boolean isWhitespace(char character) {
    return character == ' ' || character == '\t' || character == '\n';
  }

  /** Try to lex a token from the given source at the given index. */
  private static Lexeme lexToken(Source source, int index) {
    var chunk = Chunk.of(source, index);
    return switch (chunk.next()) {
      case '+' -> new Lexeme.Plus(chunk.consume().get());
      case '-' -> new Lexeme.Minus(chunk.consume().get());
      case '*' -> new Lexeme.Star(chunk.consume().get());
      case '/' -> new Lexeme.ForwardSlash(chunk.consume().get());
      case '%' -> new Lexeme.Percent(chunk.consume().get());
      case '=' -> new Lexeme.Equal(chunk.consume().get());
      case ':' -> new Lexeme.Colon(chunk.consume().get());
      case ';' -> new Lexeme.Semicolon(chunk.consume().get());
      case '(' -> new Lexeme.OpeningParentheses(chunk.consume().get());
      case ')' -> new Lexeme.ClosingParentheses(chunk.consume().get());
      case Source.EOF -> new Lexeme.EOF(chunk.consume().get());
      default -> null;
    };
  }

  /** Try to lex a number from the given source at the given index. */
  private static Lexeme lexNumber(Source source, int index) {
    final var DIGITS = "0123456789";
    final var BASE   = BigInteger.valueOf(DIGITS.length());

    var chunk = Chunk.of(source, index);
    var digit = DIGITS.indexOf(chunk.next());
    if (digit == -1) {
      return null;
    }

    var unscaled = BigInteger.valueOf(digit);
    var scale    = -1;

    while (chunk.has()) {
      chunk.consume();

      digit = DIGITS.indexOf(chunk.next());
      if (digit != -1) {
        unscaled = unscaled.multiply(BASE).add(BigInteger.valueOf(digit));
        if (scale != -1) {
          scale++;
        }
        continue;
      }

      if (chunk.next() != '.' || scale != -1) {
        break;
      }
      scale = 0;
    }

    var value = new BigDecimal(unscaled, scale);
    return new Lexeme.Number(chunk.get(), value);
  }

  /** Whether the character is letter or underscore. */
  private static boolean isLetterOrUnderscore(char character) {
    return character >= 'a' && character <= 'z'
      || character >= 'A' && character <= 'Z' || character == '_';
  }

  /** Whether the character is a decimal digit. */
  private static boolean isDecimalDigit(char character) {
    return character >= '0' && character <= '9';
  }

  /** Try to lex a word from the given source at the given index. */
  private static Lexeme lexWord(Source source, int index) {
    var chunk = Chunk.of(source, index);
    if (!Lexer.isLetterOrUnderscore(chunk.next())) {
      return null;
    }

    while (chunk.has()) {
      chunk.consume();

      if (!Lexer.isLetterOrUnderscore(chunk.next())
        && !Lexer.isDecimalDigit(chunk.next())) {
        break;
      }
    }

    var portion = chunk.get();
    return switch (portion.toString()) {
      case "i1" -> new Lexeme.I1(portion);
      case "i2" -> new Lexeme.I2(portion);
      case "i4" -> new Lexeme.I4(portion);
      case "i8" -> new Lexeme.I8(portion);
      case "ix" -> new Lexeme.Ix(portion);
      case "u1" -> new Lexeme.U1(portion);
      case "u2" -> new Lexeme.U2(portion);
      case "u4" -> new Lexeme.U4(portion);
      case "u8" -> new Lexeme.U8(portion);
      case "ux" -> new Lexeme.Ux(portion);
      case "f4" -> new Lexeme.F4(portion);
      case "f8" -> new Lexeme.F8(portion);
      case "rinf" -> new Lexeme.Rinf(portion);
      default -> new Lexeme.Identifier(portion, portion.toString());
    };
  }

  /** Report the given unknown characters to the given resolution. Returns null
   * for convinence. */
  private static Chunk report(Resolution resolution, Chunk unknown) {
    if (unknown != null) {
      resolution.error("LEXER", unknown.get(),
        "Could not recognize the character!");
    }
    return null;
  }

  /** Constructor. */
  private Lexer() {
  }
}
