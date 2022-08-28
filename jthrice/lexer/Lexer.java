// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import jthrice.Bug;
import jthrice.launcher.Source;

/** Lexes a source to a list of tokens. */
public class Lexer {
    /** Lex the given source. */
    public static ArrayList<Token> lex(Source source) {
        Lexer lexer = new Lexer(source);
        while (lexer.has()) {
            lexer.lex();
        }
        return lexer.tokens;
    }

    /** Lexed source. */
    private final Source source;
    /** Current index in the source. */
    private int index;
    /** Previously lexed tokens. */
    private ArrayList<Token> tokens;

    private Lexer(Source source) {
        this.source = source;
        index = 0;
        tokens = new ArrayList<>();
    }

    /** Whether there are characters lext to be lexed. */
    private boolean has() {
        return index < source.contents.length();
    }

    /** Currently pointed character. */
    private char current() {
        Bug.check(has(), "There are no characters!");
        return source.contents.charAt(index);
    }

    /** Lex the next token. */
    private void lex() {
        if (skipWhitespace() || lexMark() || lexNumber()) {
            return;
        }
        source.error("LEXER", Portion.of(source, index, index),
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
        tokens.add(new Token(mark.get(), character, Portion.of(source, index, index)));
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

        tokens.add(new Token(Token.Type.NUMBER, value, Portion.of(source, start, index - 1)));
        return true;
    }
}
