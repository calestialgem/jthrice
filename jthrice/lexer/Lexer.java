// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.ArrayList;
import java.util.Optional;

import jthrice.logger.Logger;
import jthrice.exception.Bug;
import jthrice.logger.Log;

/** Lexes a source to a list of tokens. */
public class Lexer {
    /** Lex the given source. */
    public static ArrayList<Token> lex(Source source, Logger logger) {
        Lexer lexer = new Lexer(source);
        while (lexer.has()) {
            lexer.lex(logger);
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
    private void lex(Logger logger) {
        if (skipWhitespace() || lexMark()) {
            return;
        }
        logger.log(source, Portion.of(source, index, index),
                "Could not recognize the character '" + current() + "'!", Log.Level.ERROR,
                "LEXER");
        index++;
    }

    /** Skip whitespace. Returns true when there is nothing left. */
    private boolean skipWhitespace() {
        while (has() && switch (current()) {
            case '\t', '\r', '\n', ' ' -> true;
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
}
