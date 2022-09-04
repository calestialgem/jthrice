// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.*;
import java.util.*;
import java.util.function.*;

import jthrice.launcher.*;
import jthrice.utility.*;
import jthrice.utility.Iterator;
import jthrice.utility.List;

/** Groups the characters in a source file to a list of lexemes. */
public final class Lexer {
  /** Lex the source in the given resolution. */
  public static List<Lexeme> lex(Resolution resolution) {
    var lexer = new Lexer(resolution);
    return lexer.lex();
  }

  /** Resolution of the lexed source. */
  private final Resolution           resolution;
  /** Current character to be lexed. */
  private Maybe<Iterator<Character>> cursor;

  private Lexer(Resolution resolution) {
    this.resolution = resolution;
    this.cursor     = Iterator
      .ofFirst(List.ofString(resolution.source.contents));
  }

  /** Consumes the current character and returns whether the next character is
   * lexable, which means it exists and its not whitespace. */
  private boolean next() {
    Function<Iterator<Character>, Boolean> whitespace = iterator -> iterator
      .get() == ' ' || iterator.get() == '\t' || iterator.get() == '\n';
    return this.cursor.map(old -> {
      this.cursor = old.next();
      return this.cursor.map(first -> {
        while (this.cursor.map(whitespace, false)) {
          this.cursor = this.cursor.bind(Iterator::next);
        }
        return !whitespace.apply(first);
      }, false);
    }, false);
  }

  /** Portion from the given first to the last character iterator. */
  private Maybe<Portion> portion(Iterator<Character> first,
    Iterator<Character> last) {
    return Portion.of(this.resolution.source, first.index, last.index);
  }

  /** Portion containing current character. */
  private Maybe<Portion> currentPortion() {
    return this.cursor.bind(current -> this.portion(current, current));
  }

  /** Lex all the source contents. */
  private List<Lexeme> lex() {
    var lexemes = new ArrayList<Lexeme>();
    while (this.cursor.exists()) {
      var lexeme = Maybe.or(this::lexToken, this::lexNumber, this::lexWord);
      lexeme.use(lexemes::add, () -> {
        this.currentPortion().use(
          portion -> this.resolution.error("LEXER", portion,
            "Could not recognize the character!"),
          () -> this.resolution.error("LEXER",
            "Try to lex but there are no characters!"));
        this.next();
      });
    }
    Bug.check(
      !lexemes.isEmpty()
        && lexemes.get(lexemes.size() - 1) instanceof Lexeme.EOF,
      "There is no EOF character at the end of the source contents!");
    Bug.check(
      lexemes.stream().filter(token -> token instanceof Lexeme.EOF)
        .count() == 1,
      "There are EOF characters in the middle of the source contents!");
    return List.of(lexemes);
  }

  /** Lex a token. */
  private Maybe<Lexeme> lexToken() {
    return this.cursor.bind(current -> switch (current.get()) {
      case '+' -> this.currentPortion().map(Lexeme.Plus::new);
      case '-' -> this.currentPortion().map(Lexeme.Minus::new);
      case '*' -> this.currentPortion().map(Lexeme.Star::new);
      case '/' -> this.currentPortion().map(Lexeme.ForwardSlash::new);
      case '%' -> this.currentPortion().map(Lexeme.Percent::new);
      case '=' -> this.currentPortion().map(Lexeme.Equal::new);
      case ':' -> this.currentPortion().map(Lexeme.Colon::new);
      case ';' -> this.currentPortion().map(Lexeme.Semicolon::new);
      case '(' -> this.currentPortion().map(Lexeme.OpeningParentheses::new);
      case ')' -> this.currentPortion().map(Lexeme.ClosingParentheses::new);
      case Source.EOF -> this.currentPortion().map(Lexeme.EOF::new);
      default -> None.<Lexeme>of();
    }).use(token -> this.next());
  }

  /** Lex a number. */
  private Maybe<Lexeme> lexNumber() {
    final var DIGITS = "0123456789";
    final var BASE   = BigInteger.valueOf(DIGITS.length());

    Function<Character, Maybe<Integer>> toDigit = character -> {
      var index = DIGITS.indexOf(character);
      if (index == -1) {
        return None.of();
      }
      return Some.of(index);
    };

    return this.cursor.bind(first -> toDigit.apply(first.get()).bind(start -> {
      final class State {
        public BigInteger          unscaled;
        public Maybe<Integer>      scale;
        public Iterator<Character> last;
        public boolean             stop;

        public State(BigInteger unscaled, Maybe<Integer> scale,
          Iterator<Character> last, boolean stop) {
          this.unscaled = unscaled;
          this.scale    = scale;
          this.last     = last;
          this.stop     = stop;
        }
      }

      var state = new State(BigInteger.valueOf(start), None.of(), first, false);

      while (this.next() && !state.stop) {
        this.cursor.use(current -> {
          toDigit.apply(current.get())
            .use(digit -> new State(
              state.unscaled.multiply(BASE).add(BigInteger.valueOf(digit)),
              state.scale.map(i -> i + 1), current, false), () -> {
                if (current.get() == '.' && state.scale.exists()) {
                  state.stop = true;
                } else {
                  state.scale = Some.of(0);
                }
              });
        });
      }

      var value = new BigDecimal(state.unscaled, state.scale.get(0));
      return this.portion(first, state.last)
        .map(portion -> new Lexeme.Number(portion, value));
    }));
  }

  /** Lex a keyword or an identifier. */
  private Maybe<Lexeme> lexWord() {
    return Maybe.tryBind(this.lexIdentifier(), Lexer::lexKeyword);
  }

  /** Lex an identifier. */
  private Maybe<Lexeme.Identifier> lexIdentifier() {
    return this.cursor.bind(first -> {
      var start = first.get();
      if ((start < 'a' || start > 'z') && (start < 'A' || start > 'Z')
        && start != '_') {
        return None.of();
      }

      final class State {
        public Iterator<Character> last;
        public boolean             stop;

        public State(Iterator<Character> last, boolean stop) {
          this.last = last;
          this.stop = stop;
        }
      }

      var state = new State(first, false);

      while (this.next() && !state.stop) {
        this.cursor.use(current -> {
          var letter = current.get();
          if ((letter < '0' || letter > '9') && (letter < 'a' || letter > 'z')
            && (letter < 'A' || letter > 'Z') && letter != '_') {
            state.stop = true;
          } else {
            state.last = current;
          }
        });
      }

      return this.portion(first, state.last)
        .map(portion -> new Lexeme.Identifier(portion, portion.toString()));
    });
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
