// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.ArrayList;

/** Lexes a source to a list of tokens. */
public class Lexer {
    /** Lex the given source. */
    public static ArrayList<Token> lex(String source) {
        Lexer lexer = new Lexer(source);
        while (lexer.has()) {
            lexer.lex();
        }
        return lexer.tokens;
    }

    /** Lexed source. */
    private final String source;
    /** Current index in the source. */
    private int index;
    /** Previously lexed tokens. */
    private ArrayList<Token> tokens;

    /** Initialize at the start of the given source with an empty lex. */
    private Lexer(String source) {
        this.source = source;
        index = 0;
        tokens = new ArrayList<>();
    }

    /** Whether there are characters lext to be lexed. */
    private boolean has() {
        return index < source.length();
    }

    /** Lex the next token. */
    private void lex() {

    }
}
