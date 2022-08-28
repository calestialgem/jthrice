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
        return lexer.tokens;
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
        if (skipWhitespace() || lexMark() || lexNumber() || lexIdentifier()) {
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
        Optional<Token.Type> mark = Token.Type.of(character);
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
        final BigDecimal BASE = new BigDecimal(10);

        int start = index;
        int digit = DIGITS.indexOf(current());
        if (digit == -1) {
            return false;
        }

        BigDecimal value = new BigDecimal(0);

        while (digit != -1) {
            value = value.multiply(BASE).add(new BigDecimal(digit));
            index++;
            if (!has()) {
                break;
            }
            digit = DIGITS.indexOf(current());
        }

        tokens.add(new Token(Token.Type.NUMBER, value, Portion.of(resolution.source, start, index - 1)));
        return true;
    }

    /** Try to lex an identifier. */
    private boolean lexIdentifier() {
        int start = index;
        char character = current();
        if ((character < 'a' || character > 'z') && (character < 'A' || character > 'Z') && character != '_') {
            return false;
        }

        StringBuilder value = new StringBuilder();

        while ((character >= '0' && character <= '9') || (character >= 'a' && character <= 'z')
                || (character >= 'A' && character <= 'Z') || character == '_') {
            value.append(character);
            index++;
            if (!has()) {
                break;
            }
            character = current();
        }

        tokens.add(new Token(Token.Type.IDENTIFIER, value, Portion.of(resolution.source, start, index - 1)));
        return true;
    }
}
