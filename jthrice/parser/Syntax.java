// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;
import java.util.Arrays;

import jthrice.Bug;
import jthrice.lexer.Token;

/** Abstract syntax tree. */
public class Syntax {
    /** Type of the syntax object. */
    public static enum Type {
        PLUS, MINUS, STAR, FORWARD_SLASH, PERCENT, EQUAL, SEMICOLON, NUMBER, IDENTIFIER, SOURCE, STATEMENT,
        STATEMENT_BODY, DEFINITION, EXPRESSION, LITERAL, UNARY_OPERATION, BINARY_OPERATION;
    };

    /** Syntax object reduction patterns. */
    public static Pattern[] PATTERNS = {
            Pattern.ofRepeatition(Type.SOURCE, Type.STATEMENT),
            Pattern.ofSerial(Type.STATEMENT, Type.STATEMENT_BODY, Type.SEMICOLON),
            Pattern.ofParallel(Type.STATEMENT_BODY, Type.DEFINITION),
            Pattern.ofSerial(Type.DEFINITION, Type.IDENTIFIER, Type.EQUAL, Type.EXPRESSION),
            Pattern.ofParallel(Type.EXPRESSION, Type.LITERAL, Type.IDENTIFIER, Type.UNARY_OPERATION,
                    Type.BINARY_OPERATION),
            Pattern.ofParallel(Type.LITERAL, Type.NUMBER),
            Pattern.ofSerial(Type.UNARY_OPERATION, Type.PLUS, Type.EXPRESSION),
            Pattern.ofSerial(Type.UNARY_OPERATION, Type.MINUS, Type.EXPRESSION),
            Pattern.ofSerial(Type.BINARY_OPERATION, Type.EXPRESSION, Type.PLUS, Type.EXPRESSION),
            Pattern.ofSerial(Type.BINARY_OPERATION, Type.EXPRESSION, Type.MINUS, Type.EXPRESSION),
            Pattern.ofSerial(Type.BINARY_OPERATION, Type.EXPRESSION, Type.STAR, Type.EXPRESSION),
            Pattern.ofSerial(Type.BINARY_OPERATION, Type.EXPRESSION, Type.FORWARD_SLASH, Type.EXPRESSION),
            Pattern.ofSerial(Type.BINARY_OPERATION, Type.EXPRESSION, Type.PERCENT, Type.EXPRESSION),
    };

    /** Reduce a token to a syntax object.. */
    public static Syntax reduce(Token token) {
        Bug.check(Type.values().length > token.type.ordinal(), "The token types are more than the syntax types!");
        return new Syntax(Type.values()[token.type.ordinal()], token);
    }

    /** An empty source syntax object. */
    public static Syntax EMPTY = new Syntax(Type.SOURCE, new Syntax[0]);

    /** Type of this syntax object. */
    public final Type type;
    /** Syntax objects that are under this one. */
    private final Syntax[] childeren;
    /** Tokens that make up the syntax object. */
    private final Token[] tokens;

    public Syntax(Type type, Syntax... childeren) {
        this.type = type;
        this.childeren = childeren;
        ArrayList<Token> tokens = new ArrayList<>();
        for (Syntax child : childeren) {
            tokens.addAll(Arrays.asList(child.tokens));
        }
        this.tokens = tokens.toArray(new Token[0]);
    }

    public Syntax(Type type, Token... tokens) {
        this.type = type;
        this.childeren = new Syntax[0];
        this.tokens = tokens;
    }

    /** Syntax objects that are under this one. */
    public Syntax[] childeren() {
        return Arrays.copyOf(childeren, childeren.length);
    }

    /** Tokens that make up the syntax object. */
    public Token[] tokens() {
        return Arrays.copyOf(tokens, tokens.length);
    }

    /** Whether the type of the syntax object is one of the given types. */
    public boolean check(Type... types) {
        for (Type type : types) {
            if (this.type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public String toString(int depth) {
        String result = "";
        for (int i = 1; i < depth; i++) {
            result += "  |";
        }
        if (depth > 0) {
            result += "  +- ";
        }
        result += type + " {";
        for (Token token : tokens) {
            result += token.portion;
        }
        result += "}\n";
        for (Syntax child : childeren) {
            result += child.toString(depth + 1);
        }
        return result;
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
