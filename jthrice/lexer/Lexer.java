// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.ArrayList;
import java.util.Optional;

import jthrice.logger.Logger;
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

    /** Initialize at the start of the given source with an empty lex. */
    private Lexer(Source source) {
        this.source = source;
        index = 0;
        tokens = new ArrayList<>();
    }

    /** Whether there are characters lext to be lexed. */
    private boolean has() {
        return index < source.contents.length();
    }

    /** Lex the next token. */
    private void lex(Logger logger) {
        if (lexMark()) {
            return;
        }
        logger.log(source, new Portion(source, index, index), "Could not recognize the character!", Log.Level.ERROR,
                "LEXER");
        index++;
    }

    /** Try to lex a mark. */
    private boolean lexMark() {
        char character = source.contents.charAt(index);
        Optional<Token.Type> mark = Token.Type.asMark(character);
        if (mark.isEmpty()) {
            return false;
        }
        tokens.add(new Token(mark.get(), character, new Portion(source, index, index + 1)));
        index++;
        return true;
    }
}
