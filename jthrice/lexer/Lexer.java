// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import jthrice.launcher.Resolution;
import jthrice.launcher.Source;
import jthrice.utility.Bug;
import jthrice.utility.Iterator;
import jthrice.utility.List;
import jthrice.utility.Result;

/** Groups the characters in a source file to a list of lexemes. */
public final class Lexer {
  /** Lex the source in the given resolution. */
  public static List<Lexeme> lex(Resolution resolution) {
    var lexer = new Lexer(resolution);
    return lexer.lex();
  }

  /** Whether the given character is whitespace. */
  public static boolean whitespace(char character) {
    return character == ' ' || character == '\t' || character == '\n';
  }

  /** Resolution of the lexed source. */
  private final Resolution            resolution;
  /** Current character to be lexed. */
  private Result<Iterator<Character>> cursor;

  private Lexer(Resolution resolution) {
    this.resolution = resolution;
    this.cursor     = Iterator
      .ofFirst(List.ofString(resolution.source.contents));
  }

  /**
   * Consumes the current character and returns whether the next character is
   * lexable, which means it exists and its not whitespace.
   */
  private boolean next() {
    this.cursor = this.cursor.get().next();
    if (this.cursor.empty()) {
      return false;
    }
    if (!Lexer.whitespace(this.cursor.get().get())) {
      return true;
    }
    while (Lexer.whitespace(this.cursor.get().get())) {
      this.cursor = this.cursor.get().next();
      if (this.cursor.empty()) {
        break;
      }
    }
    return false;
  }

  /** Portion from the given first to the last character iterator. */
  private Portion portion(Iterator<Character> first, Iterator<Character> last) {
    return new Portion(new Location(this.resolution.source, first.index),
      new Location(this.resolution.source, last.index));
  }

  /** Portion containing current character. */
  private Portion currentPortion() {
    return this.portion(this.cursor.get(), this.cursor.get());
  }

  /** Lex all the source contents. */
  private List<Lexeme> lex() {
    var lexemes = new ArrayList<Lexeme>();
    while (this.cursor.valid()) {
      var lexeme = Result.or(this::lexToken, this::lexNumber, this::lexWord);
      if (lexeme.exists()) {
        if (lexeme.valid()) {
          lexemes.add(lexeme.get());
        }
      } else {
        this.resolution.error("LEXER", this.currentPortion(),
          "Could not recognize the character!");
        this.next();
      }
    }
    Bug.check(
      !lexemes.isEmpty()
        && lexemes.get(lexemes.size() - 1) instanceof Lexeme.EOF,
      "There is no EOF character at the end of the source contents!");
    Bug.check(
      lexemes.stream().filter(token -> token instanceof Lexeme.EOF)
        .count() == 1,
      "There are EOF characters in the middle of the source contents!");
    return new List<>(lexemes);
  }

  /** Lex a token. */
  private Result<Lexeme> lexToken() {
    Result<Lexeme> token = switch (this.cursor.get().get().charValue()) {
      case '+' -> Result.of(new Lexeme.Plus(this.currentPortion()));
      case '-' -> Result.of(new Lexeme.Minus(this.currentPortion()));
      case '*' -> Result.of(new Lexeme.Star(this.currentPortion()));
      case '/' -> Result.of(new Lexeme.ForwardSlash(this.currentPortion()));
      case '%' -> Result.of(new Lexeme.Percent(this.currentPortion()));
      case '=' -> Result.of(new Lexeme.Equal(this.currentPortion()));
      case ':' -> Result.of(new Lexeme.Colon(this.currentPortion()));
      case ';' -> Result.of(new Lexeme.Semicolon(this.currentPortion()));
      case '(' ->
        Result.of(new Lexeme.OpeningParentheses(this.currentPortion()));
      case ')' ->
        Result.of(new Lexeme.ClosingParentheses(this.currentPortion()));
      case Source.EOF -> Result.of(new Lexeme.EOF(this.currentPortion()));
      default -> Result.ofUnexisting();
    };
    if (token.valid()) {
      this.next();
    }
    return token;
  }

  /** Lex a number. */
  private Result<Lexeme> lexNumber() {
    final var DIGITS = "0123456789";
    final var BASE   = BigInteger.valueOf(DIGITS.length());

    var first   = this.cursor.get();
    var current = first.get();
    var digit   = DIGITS.indexOf(current);
    if (digit == -1) {
      return Result.ofUnexisting();
    }

    var value         = BigInteger.valueOf(digit);
    var decimalPlaces = Result.<java.lang.Integer>ofUnexisting();
    var last          = first;

    while (this.next()) {
      current = this.cursor.get().get();
      if (current == '.') {
        if (decimalPlaces.exists()) {
          break;
        }
        decimalPlaces = Result.ofInvalid();
        continue;
      }

      digit = DIGITS.indexOf(current);
      if (digit == -1) {
        break;
      }

      value = value.multiply(BASE);
      value = value.add(BigInteger.valueOf(digit));
      last  = this.cursor.get();

      if (decimalPlaces.valid()) {
        decimalPlaces = Result.of(decimalPlaces.get() + 1);
      } else if (decimalPlaces.invalid()) {
        decimalPlaces = Result.of(1);
      }
    }

    if (decimalPlaces.empty()) {
      return Result.of(new Lexeme.Integer(this.portion(first, last), value));
    }
    return Result.of(new Lexeme.Real(this.portion(first, last),
      new BigDecimal(value, decimalPlaces.get())));
  }

  /** Lex a keyword or an identifier. */
  private Result<Lexeme> lexWord() {
    var identifier = this.lexIdentifier();
    if (identifier.empty()) {
      return identifier;
    }
    return Result.or(() -> lexKeyword((Lexeme.Identifier) identifier.get()),
      () -> identifier);
  }

  /** Lex an identifier. */
  private Result<Lexeme> lexIdentifier() {
    var first   = this.cursor.get();
    var current = first.get();
    if ((current < 'a' || current > 'z') && (current < 'A' || current > 'Z')
      && current != '_') {
      return Result.ofUnexisting();
    }

    var value = new StringBuilder().append(current);
    var last  = first;

    while (this.next()) {
      current = this.cursor.get().get();
      if ((current < '0' || current > '9') && (current < 'a' || current > 'z')
        && (current < 'A' || current > 'Z') && current != '_') {
        break;
      }
      value.append(current);
      last = this.cursor.get();
    }
    return Result
      .of(new Lexeme.Identifier(this.portion(first, last), value.toString()));
  }

  /** Lex a keyword. */
  private static Result<Lexeme> lexKeyword(Lexeme.Identifier identifier) {
    return switch (identifier.value) {
      case "i1" -> Result.of(new Lexeme.I1(identifier.portion));
      case "i2" -> Result.of(new Lexeme.I2(identifier.portion));
      case "i4" -> Result.of(new Lexeme.I4(identifier.portion));
      case "i8" -> Result.of(new Lexeme.I8(identifier.portion));
      case "ix" -> Result.of(new Lexeme.IX(identifier.portion));
      case "u1" -> Result.of(new Lexeme.U1(identifier.portion));
      case "u2" -> Result.of(new Lexeme.U2(identifier.portion));
      case "u4" -> Result.of(new Lexeme.U4(identifier.portion));
      case "u8" -> Result.of(new Lexeme.U8(identifier.portion));
      case "ux" -> Result.of(new Lexeme.UX(identifier.portion));
      case "f4" -> Result.of(new Lexeme.F4(identifier.portion));
      case "f8" -> Result.of(new Lexeme.F8(identifier.portion));
      default -> Result.ofUnexisting();
    };
  }
}
