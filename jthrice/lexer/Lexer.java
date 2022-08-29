// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import jthrice.Bug;
import jthrice.launcher.Resolution;

/** Lexes a source to a list of tokens. */
public class Lexer {
    /** Lex the source in the given resolution. */
    public static ArrayList<Token> lex(Resolution resolution) {
        Lexer lexer = new Lexer(resolution);
        while (lexer.has()) {
            lexer.lex();
        }
        return lexer.collect();
    }

    /** Resolution of the lexed source. */
    private final Resolution resolution;
    /** Current index in the source. */
    private int index;
    /** Previously lexed tokens. */
    private ArrayList<Token> tokens;

    private Lexer(Resolution resolution) {
        this.resolution = resolution;
        index = 0;
        tokens = new ArrayList<>();
    }

    /** Whether there are characters lext to be lexed. */
    private boolean has() {
        return index < resolution.source.contents.length();
    }

    /** Currently pointed character. */
    private char current() {
        Bug.check(has(), "There are no characters!");
        return resolution.source.contents.charAt(index);
    }

    /** Lex the next token. */
    private void lex() {
        if (skipWhitespace() || lexMark() || lexNumber() || lexKeywordOrIdentifier()) {
            return;
        }
        resolution.error("LEXER", Portion.of(resolution.source, index, index),
                "Could not recognize the character '" + current() + "'!");
        index++;
    }

    /** Skip whitespace. Returns true when there is nothing left. */
    private boolean skipWhitespace() {
        while (has() && switch (current()) {
            case '\t', '\n', ' ' -> true;
            default -> false;
        }) {
            index++;
        }
        return !has();
    }

    /** Try to lex a mark. */
    private boolean lexMark() {
        char character = current();
        Optional<Token.Type> mark = Token.Type.ofMark(character);
        if (mark.isEmpty()) {
            return false;
        }
        tokens.add(new Token(mark.get(), character, Portion.of(resolution.source, index, index)));
        index++;
        return true;
    }

    /** Try to lex a number. */
    private boolean lexNumber() {
        final String DIGITS = "0123456789";
        final BigDecimal BASE = new BigDecimal(DIGITS.length());

        int start = index;
        int digit = DIGITS.indexOf(current());
        if (digit == -1) {
            return false;
        }

        int decimalPlaces = -1;
        BigDecimal value = new BigDecimal(0);

        while (digit != -1) {
            value = value.multiply(BASE).add(new BigDecimal(digit));
            if (decimalPlaces != -1) {
                decimalPlaces++;
            }
            index++;
            if (!has()) {
                break;
            }
            if (current() == '.') {
                decimalPlaces = 0;
                index++;
            }
            digit = DIGITS.indexOf(current());
        }

        Portion portion = Portion.of(resolution.source, start, index - 1);

        if (decimalPlaces == 0) {
            resolution.error("LEXER", portion,
                    "The number ends after the decimal place separator without a digit in decimal place!");
        }

        if (decimalPlaces != -1) {
            BigDecimal dividor = BASE.pow(decimalPlaces);
            value = value.divide(dividor);
        }

        tokens.add(new Token(Token.Type.NUMBER, value, portion));
        return true;
    }

    /** Try to lex a keyword or an identifier. */
    private boolean lexKeywordOrIdentifier() {
        int start = index;
        char character = current();
        if ((character < 'a' || character > 'z') && (character < 'A' || character > 'Z') && character != '_') {
            return false;
        }

        StringBuilder builder = new StringBuilder();

        while ((character >= '0' && character <= '9') || (character >= 'a' && character <= 'z')
                || (character >= 'A' && character <= 'Z') || character == '_') {
            builder.append(character);
            index++;
            if (!has()) {
                break;
            }
            character = current();
        }

        String value = builder.toString();
        Optional<Token.Type> keyword = Token.Type.ofKeyword(value);
        if (keyword.isPresent()) {
            tokens.add(new Token(keyword.get(), value, Portion.of(resolution.source, start, index - 1)));

        } else {
            tokens.add(new Token(Token.Type.IDENTIFIER, value, Portion.of(resolution.source, start, index - 1)));
        }
        return true;
    }

    /** Result of lexing. */
    private ArrayList<Token> collect() {
        Bug.check(!has(), "There are still characters that are not lexed!");
        Bug.check(!tokens.isEmpty() && tokens.get(tokens.size() - 1).check(Token.Type.EOF),
                "There is no EOF character at the end of the source!");
        Bug.check(tokens.stream().filter(token -> token.check(Token.Type.EOF)).count() == 1,
                "There are EOF characters in the middle of the source!");
        return tokens;
    }
}
