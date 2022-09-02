// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import jthrice.launcher.Resolution;
import jthrice.launcher.Source;
import jthrice.utility.Bug;
import jthrice.utility.List;
import jthrice.utility.Result;

/** Lexes a source to a list of lexemes. */
public class Lexer {
    /** Lex the source in the given resolution. */
    public static List<Lexeme> lex(Resolution resolution) {
        var lexer = new Lexer(resolution);
        return lexer.lex();
    }

    /** Resolution of the lexed source. */
    private final Resolution resolution;
    /** Current source location to be lexed. */
    private Result<Location> cursor;

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
        if (cursor.empty()) {
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
    private List<Lexeme> lex() {
        var tokens = new ArrayList<Lexeme>();
        while (cursor.valid()) {
            if (consume('\t', ' ')) {
                continue;
            }
            var token = Result.or(lexMark(), lexNumber(), lexWord());
            if (token.valid()) {
                tokens.add(token.get());
                continue;
            }
            error("Could not lex the character!");
        }
        Bug.check(!tokens.isEmpty() && tokens.get(tokens.size() - 1) instanceof Lexeme.Token.EOF,
                "There is no EOF character at the end of the source contents!");
        Bug.check(tokens.stream().filter(token -> token instanceof Lexeme.Token.EOF).count() == 1,
                "There are EOF characters in the middle of the source contents!");
        return new List<>(tokens);
    }

    /** Lex a mark. */
    private Result<Lexeme> lexMark() {
        if (cursor.empty()) {
            return Result.ofUnexisting();
        }
        Location first = cursor.get();
        return switch (first.get()) {
            case '+' -> Result.of(new Lexeme.Token.Plus(new Portion(first, first)));
            case '-' -> Result.of(new Lexeme.Token.Minus(new Portion(first, first)));
            case '*' -> Result.of(new Lexeme.Token.Star(new Portion(first, first)));
            case '/' -> Result.of(new Lexeme.Token.ForwardSlash(new Portion(first, first)));
            case '%' -> Result.of(new Lexeme.Token.Percent(new Portion(first, first)));
            case '=' -> Result.of(new Lexeme.Token.Equal(new Portion(first, first)));
            case ':' -> Result.of(new Lexeme.Token.Colon(new Portion(first, first)));
            case ';' -> Result.of(new Lexeme.Token.Semicolon(new Portion(first, first)));
            case '(' -> Result.of(new Lexeme.Token.OpeningBracket(new Portion(first, first)));
            case ')' -> Result.of(new Lexeme.Token.ClosingBracket(new Portion(first, first)));
            case Source.EOF -> Result.of(new Lexeme.Token.EOF(new Portion(first, first)));
            default -> Result.ofUnexisting();
        };
    }

    /** Lex a number. */
    private Result<Lexeme> lexNumber() {
        final var DIGITS = "0123456789";
        final var BASE = BigInteger.valueOf(DIGITS.length());

        Location first = cursor.get();
        var current = first;
        var digit = DIGITS.indexOf(current.get());
        if (digit == -1) {
            return Result.ofUnexisting();
        }

        var decimalPlaces = Result.<java.lang.Integer>empty();
        var value = BigInteger.valueOf(0);
        while (digit != -1) {
            value = value.multiply(BASE).add(BigInteger.valueOf(digit));
            if (decimalPlaces.valid()) {
                decimalPlaces = Result.of(decimalPlaces.get() + 1);
            }
            var next = current.next();
            if (next.empty()) {
                break;
            }
            current = next.get();
            if (current.get() == '.') {
                if (decimalPlaces.valid()) {
                    return Result.ofUnexisting();
                }
                decimalPlaces = Result.of(0);
                next = current.next();
                if (next.empty()) {
                    return Result.ofUnexisting();
                }
                current = next.get();
            }
            digit = DIGITS.indexOf(current.get());
        }

        var portion = new Portion(first, current.previous().get());

        if (decimalPlaces.empty()) {
            return Result.of(new Lexeme.Number.Integer(portion, value));
        }
        if (decimalPlaces.get() == 0) {
            return Result.ofUnexisting();
        }
        return Result.of(new Lexeme.Number.Real(portion, new BigDecimal(value, decimalPlaces.get())));
    }

    /** Lex a keyword or identifier. */
    private Result<Lexeme> lexWord() {
        return Result.ofUnexisting();
    }
}
