// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import jthrice.launcher.Resolution;
import jthrice.launcher.Source;
import jthrice.utility.Bug;
import jthrice.utility.List;

/** Lexes a source to a list of tokens. */
public class Lexer {
    /** Lex the source in the given resolution. */
    public static List<Token> lex(Resolution resolution) {
        var lexer = new Lexer(resolution);
        return lexer.lex();
    }

    /** Resolution of the lexed source. */
    private final Resolution resolution;
    /** Current source location to be lexed. */
    private Optional<Location> cursor;

    private Lexer(Resolution resolution) {
        this.resolution = resolution;
        cursor = Location.ofFirst(resolution.source);
    }

    /** Give an error to the resolution about the current character. */
    private void error(String message) {
        resolution.error("LEXER", new Portion(cursor.get(), cursor.get()), message);
    }

    /** Move to the next character. */
    private void next() {
        cursor = cursor.get().next();
    }

    /**
     * Whether the current character exists and its in the given set. Consume the
     * character if it is.
     */
    private boolean consume(char... set) {
        if (cursor.isEmpty()) {
            return false;
        }
        for (var element : set) {
            if (element == cursor.get().get()) {
                next();
                return true;
            }
        }
        return false;
    }

    /** Lex the source file. */
    private List<Token> lex() {
        var tokens = new ArrayList<Token>();
        while (cursor.isPresent()) {
            if (consume('\t', ' ')) {
                continue;
            }
            var token = Optional.<Token>empty().or(this::lexMark).or(this::lexNumber).or(this::lexWord);
            if (token.isPresent()) {
                tokens.add(token.get());
                continue;
            }
            error("Could not lex the character!");
        }
        Bug.check(!tokens.isEmpty() && tokens.get(tokens.size() - 1) instanceof Token.Mark.EOF,
                "There is no EOF character at the end of the source contents!");
        Bug.check(tokens.stream().filter(token -> token instanceof Token.Mark.EOF).count() == 1,
                "There are EOF characters in the middle of the source contents!");
        return new List<>(tokens);
    }

    /** Lex a mark. */
    private Optional<Token> lexMark() {
        if (cursor.isEmpty()) {
            return Optional.empty();
        }
        Location first = cursor.get();
        return switch (first.get()) {
            case '+' -> Optional.of(new Token.Mark.Plus(new Portion(first, first)));
            case '-' -> Optional.of(new Token.Mark.Minus(new Portion(first, first)));
            case '*' -> Optional.of(new Token.Mark.Star(new Portion(first, first)));
            case '/' -> Optional.of(new Token.Mark.ForwardSlash(new Portion(first, first)));
            case '%' -> Optional.of(new Token.Mark.Percent(new Portion(first, first)));
            case '=' -> Optional.of(new Token.Mark.Equal(new Portion(first, first)));
            case ':' -> Optional.of(new Token.Mark.Colon(new Portion(first, first)));
            case ';' -> Optional.of(new Token.Mark.Semicolon(new Portion(first, first)));
            case '(' -> Optional.of(new Token.Mark.OpeningBracket(new Portion(first, first)));
            case ')' -> Optional.of(new Token.Mark.ClosingBracket(new Portion(first, first)));
            case Source.EOF -> Optional.of(new Token.Mark.EOF(new Portion(first, first)));
            default -> Optional.empty();
        };
    }

    /** Lex a number. */
    private Optional<Token> lexNumber() {
        final var DIGITS = "0123456789";
        final var BASE = BigInteger.valueOf(DIGITS.length());

        Location first = cursor.get();
        var current = first;
        var digit = DIGITS.indexOf(current.get());
        if (digit == -1) {
            return Optional.empty();
        }

        var decimalPlaces = Optional.<java.lang.Integer>empty();
        var value = BigInteger.valueOf(0);
        while (digit != -1) {
            value = value.multiply(BASE).add(BigInteger.valueOf(digit));
            if (decimalPlaces.isPresent()) {
                decimalPlaces = Optional.of(decimalPlaces.get() + 1);
            }
            var next = current.next();
            if (next.isEmpty()) {
                break;
            }
            current = next.get();
            if (current.get() == '.') {
                if (decimalPlaces.isPresent()) {
                    return Optional.empty();
                }
                decimalPlaces = Optional.of(0);
                next = current.next();
                if (next.isEmpty()) {
                    return Optional.empty();
                }
                current = next.get();
            }
            digit = DIGITS.indexOf(current.get());
        }

        var portion = new Portion(first, current.previous().get());

        if (decimalPlaces.isEmpty()) {
            return Optional.of(new Token.Number.Integer(portion, value));
        }
        if (decimalPlaces.get() == 0) {
            return Optional.empty();
        }
        return Optional.of(new Token.Number.Real(portion, new BigDecimal(value, decimalPlaces.get())));
    }

    /** Lex a keyword or identifier. */
    private Optional<Token> lexWord() {
        return Optional.empty();
    }
}
