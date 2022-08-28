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
        SOURCE, STATEMENT, DEFINITION_STATEMENT, DEFINITION_SIGN, EXPRESSION, BINARY_EXPRESSION, BINARY_OPERATOR,
        UNARY_EXPRESSION,
        UNARY_OPERATOR, REFERENCE, LITERAL;
    };

    /** Whether the given token is one of the given types. */
    public static boolean check(Token token, Token.Type... types) {
        for (Token.Type type : types) {
            if (token.type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    /** Whether the given syntax is one of the given types. */
    public static boolean check(Syntax syntax, Type... types) {
        for (Type type : types) {
            if (syntax.type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    /** Syntax object of a source. */
    public static Syntax ofSource(Syntax... statements) {
        for (Syntax statement : statements) {
            Bug.check(check(statement, Type.STATEMENT), "The object is not a statement!");
        }
        return new Syntax(Type.SOURCE, statements);
    }

    /** Syntax object of a statement. */
    public static Syntax ofStatement(Syntax statement) {
        Bug.check(check(statement, Type.DEFINITION_STATEMENT), "The object is not a statement!");
        return new Syntax(Type.STATEMENT, statement);
    }

    /** Syntax object of a definition statement. */
    public static Syntax ofDefinitionStatement(Syntax name, Syntax sign, Syntax expression) {
        Bug.check(check(name, Type.REFERENCE), "The object is not a name!");
        Bug.check(check(sign, Type.DEFINITION_SIGN), "The object is not a definition sign!");
        Bug.check(check(expression, Type.EXPRESSION), "The object is not an expression!");
        return new Syntax(Type.DEFINITION_STATEMENT, name, sign, expression);
    }

    /** Syntax object of a definition sign. */
    public static Syntax ofDefinitionSign(Token sign) {
        Bug.check(check(sign, Token.Type.EQUAL), "The token is not a definition sign!");
        return new Syntax(Type.DEFINITION_SIGN, sign);
    }

    /** Syntax object of an expression. */
    public static Syntax ofExpression(Syntax expression) {
        Bug.check(check(expression, Type.BINARY_EXPRESSION, Type.UNARY_EXPRESSION, Type.REFERENCE, Type.LITERAL),
                "The object is not an expression!");
        return new Syntax(Type.EXPRESSION, expression);
    }

    /** Syntax object of a binary expression. */
    public static Syntax ofBinaryExpression(Syntax left, Syntax operator, Syntax right) {
        Bug.check(check(left, Type.EXPRESSION), "The object is not an expression!");
        Bug.check(check(operator, Type.BINARY_OPERATOR), "The object is not a binary operator!");
        Bug.check(check(right, Type.EXPRESSION), "The object is not an expression!");
        return new Syntax(Type.BINARY_EXPRESSION, left, operator, right);
    }

    /** Syntax object of a binary operator. */
    public static Syntax ofOperator(Token operator) {
        Bug.check(check(operator, Token.Type.PLUS, Token.Type.MINUS, Token.Type.STAR, Token.Type.FORWARD_SLASH,
                Token.Type.PERCENT), "The token is not a binary operator!");
        return new Syntax(Type.BINARY_OPERATOR, operator);
    }

    /** Syntax object of a unary expressison. */
    public static Syntax ofUnaryExpressison(Syntax operator, Syntax expression) {
        Bug.check(check(operator, Type.UNARY_OPERATOR), "The object is not a unary operator!");
        Bug.check(check(expression, Type.EXPRESSION), "The object is not an expression!");
        return new Syntax(Type.UNARY_EXPRESSION, operator, expression);
    }

    /** Syntax object of a unary operator. */
    public static Syntax ofUnaryOperator(Token operator) {
        Bug.check(check(operator, Token.Type.PLUS, Token.Type.MINUS), "The token is not a unary operator!");
        return new Syntax(Type.UNARY_OPERATOR, operator);
    }

    /** Syntax object of a reference. */
    public static Syntax ofReference(Token reference) {
        Bug.check(check(reference, Token.Type.IDENTIFIER), "The token is not a reference!");
        return new Syntax(Type.REFERENCE, reference);
    }

    /** Syntax object of a literal token. */
    public static Syntax ofLiteral(Token literal) {
        Bug.check(check(literal, Token.Type.NUMBER), "The token is not a literal!");
        return new Syntax(Type.LITERAL, literal);
    }

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
}
