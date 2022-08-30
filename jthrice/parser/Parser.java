// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;
import java.util.Optional;

import jthrice.Bug;
import jthrice.launcher.Resolution;
import jthrice.lexer.Lexer;
import jthrice.lexer.Token;

/** Parses a list of tokens to syntatic symbols. */
public class Parser {
    /** Parse the source in the given resolution. */
    public static Symbol parse(Resolution resolution) {
        Parser parser = new Parser(resolution, Lexer.lex(resolution));
        while (parser.parse()) {
        }
        return parser.collect();
    }

    /** Resolution of the parsed tokens. */
    private final Resolution resolution;
    /** Tokens to be parsed. */
    private final ArrayList<Token> tokens;
    /** Current index in the tokens. */
    private int index;
    /** Symbols on the stack. */
    private final Stack stack;

    private Parser(Resolution resolution, ArrayList<Token> tokens) {
        this.resolution = resolution;
        this.tokens = tokens;
        index = 0;
        stack = new Stack();
    }

    /** Whether there are any tokens left. */
    private boolean has() {
        return index < tokens.size();
    }

    /** Parse a token. Returns whether there are actions to be done. */
    private boolean parse() {
        Optional<Symbol.Result> result = Symbol.of(stack);
        if (result.isEmpty()) {
            if (!has()) {
                return false;
            }
            stack.shift(tokens.get(index));
            index++;
            return true;
        }

        stack.reduce(result.get());
        return true;
    }

    /** Result of parsing. */
    private Symbol collect() {
        Bug.check(!has(), "There are still tokens that are not parsed!");
        switch (stack.size()) {
            case 0:
                Bug.check(false, "There is no EOF in the source!");
            case 1:
                return stack.top(0).get();
            default:
                resolution.error("PARSER", "Parse objects failed to reduce!");
                return stack.top(0).get();
        }
    }
}
