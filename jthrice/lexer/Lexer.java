// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.ArrayList;
import java.util.Optional;

import jthrice.Bug;
import jthrice.launcher.Resolution;

/** Lexes a source to a list of tokens. */
public class Lexer {
    /** Lex the source in the given resolution. */
    public static Token[] lex(Resolution resolution) {
        var lexer = new Lexer(resolution);
        while (lexer.has()) {
            lexer.lex();
        }
        return lexer.collect();
    }

    /** Resolution of the lexed source. */
    private final Resolution resolution;
    /** Current location in the source. */
    private Optional<Location> current;
    /** Previously lexed tokens. */
    private ArrayList<Token> tokens;

    private Lexer(Resolution resolution) {
        this.resolution = resolution;
        current = Location.ofFirst(resolution.source);
        tokens = new ArrayList<>();
    }

    /** Whether there are characters to be lexed. */
    private boolean has() {
        return current.isPresent();
    }

    /** Lex the next token. */
    private void lex() {
        skipWhitespace();
        if (current.isEmpty()) {
            return;
        }
        var token = Token.of(current.get());
        if (token.isPresent()) {
            tokens.add(token.get());
            current = token.get().portion.last.next();
            return;
        }
        resolution.error("LEXER", new Portion(current.get(), current.get()),
                "Could not recognize the character '" + current.get().get() + "'!");
        current = current.get().next();
    }

    /** Skip whitespace. */
    private void skipWhitespace() {
        while (current.isPresent() && switch (current.get().get()) {
            case '\t', '\n', ' ' -> true;
            default -> false;
        }) {
            current = current.get().next();
        }
    }

    /** Result of lexing. */
    private Token[] collect() {
        Bug.check(!has(), "There are still characters that are not lexed!");
        Bug.check(!tokens.isEmpty() && tokens.get(tokens.size() - 1) instanceof Token.Mark.EOF,
                "There is no EOF character at the end of the source!");
        Bug.check(tokens.stream().filter(token -> token instanceof Token.Mark.EOF).count() == 1,
                "There are EOF characters in the middle of the source!");
        return tokens.toArray(new Token[0]);
    }
}
