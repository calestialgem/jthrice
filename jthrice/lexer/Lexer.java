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
    private final Resolution resolution;
    /** Current character to be lexed. */
    private Result<Iterator<Character>> cursor;
    /** Accumulator of lex results. */
    private ArrayList<Lexeme> accumulator;

    private Lexer(Resolution resolution) {
        this.resolution = resolution;
        cursor = Iterator.ofFirst(List.ofString(resolution.source.contents));
        accumulator = new ArrayList<>();
    }

    /**
     * Consumes the current character and returns whether the next character is
     * lexable, which means it exists and its not whitespace.
     */
    private boolean next() {
        cursor = cursor.get().next();
        if (cursor.empty()) {
            return false;
        }
        if (!whitespace(cursor.get().get())) {
            return true;
        }
        while (whitespace(cursor.get().get())) {
            cursor = cursor.get().next();
            if (cursor.empty()) {
                break;
            }
        }
        return false;
    }

    /** Portion from the given first to the last character iterator. */
    private Portion portion(Iterator<Character> first, Iterator<Character> last) {
        return new Portion(new Location(resolution.source, first.index), new Location(resolution.source, last.index));
    }

    /** Portion containing current character. */
    private Portion currentPortion() {
        return portion(cursor.get(), cursor.get());
    }

    /** Lex all the source contents. */
    private List<Lexeme> lex() {
        while (cursor.valid()) {
            var lexeme = Result.or(lexToken(), lexNumber(), lexWord());
            if (lexeme.exists()) {
                if (lexeme.valid()) {
                    accumulator.add(lexeme.get());
                }
            } else {
                resolution.error("LEXER", currentPortion(), "Could not recognize the character!");
                next();
            }
        }
        Bug.check(!accumulator.isEmpty() && accumulator.get(accumulator.size() - 1) instanceof Lexeme.Token.EOF,
                "There is no EOF character at the end of the source contents!");
        Bug.check(accumulator.stream().filter(token -> token instanceof Lexeme.Token.EOF).count() == 1,
                "There are EOF characters in the middle of the source contents!");
        return new List<>(accumulator);
    }

    /** Lex a token. */
    private Result<Lexeme> lexToken() {
        return switch (cursor.get().get()) {
            case '+' -> Result.of(new Lexeme.Token.Plus(currentPortion()));
            case '-' -> Result.of(new Lexeme.Token.Minus(currentPortion()));
            case '*' -> Result.of(new Lexeme.Token.Star(currentPortion()));
            case '/' -> Result.of(new Lexeme.Token.ForwardSlash(currentPortion()));
            case '%' -> Result.of(new Lexeme.Token.Percent(currentPortion()));
            case '=' -> Result.of(new Lexeme.Token.Equal(currentPortion()));
            case ':' -> Result.of(new Lexeme.Token.Colon(currentPortion()));
            case ';' -> Result.of(new Lexeme.Token.Semicolon(currentPortion()));
            case '(' -> Result.of(new Lexeme.Token.OpeningParentheses(currentPortion()));
            case ')' -> Result.of(new Lexeme.Token.ClosingParentheses(currentPortion()));
            case Source.EOF -> Result.of(new Lexeme.Token.EOF(currentPortion()));
            default -> Result.ofUnexisting();
        };
    }

    /** Lex a number. */
    private Result<Lexeme> lexNumber() {
        final var DIGITS = "0123456789";
        final var BASE = BigInteger.valueOf(DIGITS.length());

        var first = cursor.get();
        var current = first.get();
        var digit = DIGITS.indexOf(current);
        if (digit == -1) {
            return Result.ofUnexisting();
        }

        var value = BigInteger.valueOf(digit);
        var decimalPlaces = Result.<java.lang.Integer>ofUnexisting();
        var last = first;

        while (next()) {
            current = cursor.get().get();
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
            last = cursor.get();

            if (decimalPlaces.valid()) {
                decimalPlaces = Result.of(decimalPlaces.get() + 1);
            } else if (decimalPlaces.invalid()) {
                decimalPlaces = Result.of(1);
            }
        }

        if (decimalPlaces.empty()) {
            return Result.of(new Lexeme.Number.Integer(portion(first, last), value));
        }
        return Result.of(new Lexeme.Number.Real(portion(first, last), new BigDecimal(value, decimalPlaces.get())));
    }

    /** Lex a keyword or an identifier. */
    private Result<Lexeme> lexWord() {
        var identifier = lexIdentifier();
        if (identifier.empty()) {
            return identifier;
        }
        return Result.or(lexKeyword((Lexeme.Identifier) identifier.get()), identifier);
    }

    /** Lex an identifier. */
    private Result<Lexeme> lexIdentifier() {
        var first = cursor.get();
        var current = first.get();
        if ((current < 'a' || current > 'z') && (current < 'A' || current > 'Z') && current != '_') {
            return Result.ofUnexisting();
        }

        var value = new StringBuilder().append(current);
        var last = first;

        while (next()) {
            current = cursor.get().get();
            if ((current < '0' || current > '9') && (current < 'a' || current > 'z') && (current < 'A' || current > 'Z')
                    && current != '_') {
                break;
            }
            value.append(current);
            last = cursor.get();
        }
        return Result.of(new Lexeme.Identifier(portion(first, last), value.toString()));
    }

    /** Lex a keyword. */
    private Result<Lexeme> lexKeyword(Lexeme.Identifier identifier) {
        return switch (identifier.value) {
            case "i1" -> Result.of(new Lexeme.Keyword.I1(identifier.portion));
            case "i2" -> Result.of(new Lexeme.Keyword.I2(identifier.portion));
            case "i4" -> Result.of(new Lexeme.Keyword.I4(identifier.portion));
            case "i8" -> Result.of(new Lexeme.Keyword.I8(identifier.portion));
            case "ix" -> Result.of(new Lexeme.Keyword.IX(identifier.portion));
            case "u1" -> Result.of(new Lexeme.Keyword.U1(identifier.portion));
            case "u2" -> Result.of(new Lexeme.Keyword.U2(identifier.portion));
            case "u4" -> Result.of(new Lexeme.Keyword.U4(identifier.portion));
            case "u8" -> Result.of(new Lexeme.Keyword.U8(identifier.portion));
            case "ux" -> Result.of(new Lexeme.Keyword.UX(identifier.portion));
            case "f4" -> Result.of(new Lexeme.Keyword.F4(identifier.portion));
            case "f8" -> Result.of(new Lexeme.Keyword.F8(identifier.portion));
            default -> Result.ofUnexisting();
        };
    }
}
