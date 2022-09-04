// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.*;
import java.util.*;

import jthrice.launcher.*;
import jthrice.utility.*;

/** Groups the characters in a source file to a list of lexemes. */
public final class Lexer {
  /** Lex the source in the given resolution. */
  public static FixedList<Lexeme> lex(Resolution resolution) {
    var lexer = new Lexer(resolution);
    return lexer.lex();
  }

  /** Whether the given character is whitespace. */
  public static boolean whitespace(char character) {
    return character == ' ' || character == '\t' || character == '\n';
  }

  /** Resolution of the lexed source. */
  private final Resolution                resolution;
  /** Current character to be lexed. */
  private Maybe<FixedIterator<Character>> cursor;

  private Lexer(Resolution resolution) {
    this.resolution = resolution;
    this.cursor     = FixedIterator
      .ofFirst(FixedList.ofString(resolution.source.contents));
  }

  /**
   * Consumes the current character and returns whether the next character is
   * lexable, which means it exists and its not whitespace.
   */
  private boolean next() {
    this.cursor = this.cursor.get().next();
    var result = this.cursor.is();
    while (this.cursor.is() && Lexer.whitespace(this.cursor.get().get())) {
      this.cursor = this.cursor.get().next();
      result      = false;
    }
    return result;
  }

  /** Portion from the given first to the last character iterator. */
  private Portion portion(FixedIterator<Character> first,
    FixedIterator<Character> last) {
    return new Portion(new Location(this.resolution.source, first.index),
      new Location(this.resolution.source, last.index));
  }

  /** Portion containing current character. */
  private Portion currentPortion() {
    return this.portion(this.cursor.get(), this.cursor.get());
  }

  /** Lex all the source contents. */
  private FixedList<Lexeme> lex() {
    var lexemes = new ArrayList<Lexeme>();
    while (this.cursor.is()) {
      var lexeme = Maybe.or(this::lexToken, this::lexNumber, this::lexWord);
      if (lexeme.is()) {
        lexemes.add(lexeme.get());
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
    return FixedList.of(lexemes);
  }

  /** Lex a token. */
  private Maybe<Lexeme> lexToken() {
    Maybe<Lexeme> token = switch (this.cursor.get().get().charValue()) {
      case '+' -> Some.of(new Lexeme.Plus(this.currentPortion()));
      case '-' -> Some.of(new Lexeme.Minus(this.currentPortion()));
      case '*' -> Some.of(new Lexeme.Star(this.currentPortion()));
      case '/' -> Some.of(new Lexeme.ForwardSlash(this.currentPortion()));
      case '%' -> Some.of(new Lexeme.Percent(this.currentPortion()));
      case '=' -> Some.of(new Lexeme.Equal(this.currentPortion()));
      case ':' -> Some.of(new Lexeme.Colon(this.currentPortion()));
      case ';' -> Some.of(new Lexeme.Semicolon(this.currentPortion()));
      case '(' -> Some.of(new Lexeme.OpeningParentheses(this.currentPortion()));
      case ')' -> Some.of(new Lexeme.ClosingParentheses(this.currentPortion()));
      case Source.EOF -> Some.of(new Lexeme.EOF(this.currentPortion()));
      default -> None.of();
    };
    if (token.is()) {
      this.next();
    }
    return token;
  }

  /** Lex a number. */
  private Maybe<Lexeme> lexNumber() {
    final var DIGITS = "0123456789";
    final var BASE   = BigInteger.valueOf(DIGITS.length());

    var first   = this.cursor.get();
    var current = first.get();
    var digit   = DIGITS.indexOf(current);
    if (digit == -1) {
      return None.of();
    }

    var unscaled      = BigInteger.valueOf(digit);
    var decimalPlaces = None.<Integer>of();
    var last          = first;

    while (this.next()) {
      current = this.cursor.get().get();
      if (current == '.') {
        if (decimalPlaces.is()) {
          break;
        }
        decimalPlaces = Some.of(0);
        continue;
      }

      digit = DIGITS.indexOf(current);
      if (digit == -1) {
        break;
      }

      unscaled = unscaled.multiply(BASE);
      unscaled = unscaled.add(BigInteger.valueOf(digit));
      last     = this.cursor.get();

      if (decimalPlaces.is()) {
        decimalPlaces = Some.of(decimalPlaces.get() + 1);
      }
    }

    var value = new BigDecimal(unscaled, switch (decimalPlaces) {
      case Some<Integer> some -> some.value;
      default -> 0;
    });
    return Some.of(new Lexeme.Number(this.portion(first, last), value));
  }

  /** Lex a keyword or an identifier. */
  private Maybe<Lexeme> lexWord() {
    var identifier = this.lexIdentifier();
    if (identifier.not()) {
      return identifier;
    }
    return Maybe.or(
      () -> Lexer.lexKeyword((Lexeme.Identifier) identifier.get()),
      () -> identifier);
  }

  /** Lex an identifier. */
  private Maybe<Lexeme> lexIdentifier() {
    var first   = this.cursor.get();
    var current = first.get();
    if ((current < 'a' || current > 'z') && (current < 'A' || current > 'Z')
      && current != '_') {
      return None.of();
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
    return Some
      .of(new Lexeme.Identifier(this.portion(first, last), value.toString()));
  }

  /** Lex a keyword. */
  private static Maybe<Lexeme> lexKeyword(Lexeme.Identifier identifier) {
    return switch (identifier.value) {
      case "i1" -> Some.of(new Lexeme.I1(identifier.portion));
      case "i2" -> Some.of(new Lexeme.I2(identifier.portion));
      case "i4" -> Some.of(new Lexeme.I4(identifier.portion));
      case "i8" -> Some.of(new Lexeme.I8(identifier.portion));
      case "ix" -> Some.of(new Lexeme.IX(identifier.portion));
      case "u1" -> Some.of(new Lexeme.U1(identifier.portion));
      case "u2" -> Some.of(new Lexeme.U2(identifier.portion));
      case "u4" -> Some.of(new Lexeme.U4(identifier.portion));
      case "u8" -> Some.of(new Lexeme.U8(identifier.portion));
      case "ux" -> Some.of(new Lexeme.UX(identifier.portion));
      case "f4" -> Some.of(new Lexeme.F4(identifier.portion));
      case "f8" -> Some.of(new Lexeme.F8(identifier.portion));
      case "rinf" -> Some.of(new Lexeme.Rinf(identifier.portion));
      default -> None.of();
    };
  }
}
