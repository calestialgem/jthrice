// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;

import jthrice.Bug;
import jthrice.launcher.Resolution;
import jthrice.lexer.Lexer;
import jthrice.lexer.Token;

/** Parses a list of tokens to a syntax tree. */
public class Parser {
    /** Parse the source in the given resolution. */
    public static Syntax parse(Resolution resolution) {
        Parser parser = new Parser(resolution, Lexer.lex(resolution));
        while (parser.has()) {
            parser.parse();
        }
        return parser.collect();
    }

    /** Resolution of the parsed tokens. */
    private final Resolution resolution;
    /** Tokens to be parsed. */
    private final ArrayList<Token> tokens;
    /** Current index in the tokens. */
    private int index;
    /** Syntax object stack. */
    private final ArrayList<Syntax> stack;

    private Parser(Resolution resolution, ArrayList<Token> tokens) {
        this.resolution = resolution;
        this.tokens = tokens;
        index = 0;
        stack = new ArrayList<>();
    }

    /** Whether there are any tokens left. */
    private boolean has() {
        return index < tokens.size();
    }

    /** Currently pointed token. */
    private Token current() {
        Bug.check(has(), "There are no tokens!");
        return tokens.get(index);
    }

    /** Parse a token. */
    private void parse() {
        int maxMatch = Integer.MIN_VALUE;
        Pattern greedyPattern = null;
        for (Pattern pattern : Syntax.PATTERNS) {
            int match = pattern.match(stack);
            if (match > maxMatch) {
                maxMatch = match;
                greedyPattern = pattern;
            }
        }
        Bug.check(greedyPattern != null, "There are no patterns!");
        if (maxMatch <= 0) {
            stack.add(Syntax.reduce(current()));
            index++;
            return;
        }
        Syntax[] input = new Syntax[maxMatch];
        for (int i = 0; i < maxMatch; i++) {
            int index = stack.size() - maxMatch + i;
            input[i] = stack.get(index);
            stack.remove(index);
        }
        Syntax output = new Syntax(greedyPattern.result(), input);
        stack.add(output);
    }

    private Syntax collect() {
        Bug.check(!has(), "There are still tokens that are not parsed!");
        switch (stack.size()) {
            case 0:
                return Syntax.EMPTY;
            case 1:
                return stack.get(0);
            default:
                resolution.error("PARSER", "Parse objects failed to reduce!");
                return stack.get(0);
        }
    }
}
