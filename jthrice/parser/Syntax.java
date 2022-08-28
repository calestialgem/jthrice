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
        SOURCE, STATEMENT, STATEMENT_SEPARATOR, DEFINITION_STATEMENT, DEFINITION_SIGN, EXPRESSION, BINARY_EXPRESSION,
        BINARY_OPERATOR, UNARY_EXPRESSION, UNARY_OPERATOR, REFERENCE, LITERAL;
    };

    /** Syntax object of a source. */
    public static Syntax ofSource(Syntax... statements) {
        for (Syntax statement : statements) {
            Bug.check(statement.check(Type.STATEMENT), "The object is not a statement!");
        }
        return new Syntax(Type.SOURCE, statements);
    }

    /** Syntax object of a statement. */
    public static Syntax ofStatement(Syntax body, Syntax separator) {
        Bug.check(body.check(Type.DEFINITION_STATEMENT), "The object is not a statement body!");
        Bug.check(separator.check(Type.STATEMENT_SEPARATOR), "The object is not a statement separator!");
        return new Syntax(Type.STATEMENT, body, separator);
    }

    /** Syntax object of a statement separator. */
    public static Syntax ofStatementSeparator(Token separator) {
        Bug.check(separator.check(Token.Type.SEMICOLON), "The token is not a statement separator!");
        return new Syntax(Type.STATEMENT_SEPARATOR, separator);
    }

    /** Syntax object of a definition statement. */
    public static Syntax ofDefinitionStatement(Syntax name, Syntax sign, Syntax expression) {
        Bug.check(name.check(Type.REFERENCE), "The object is not a name!");
        Bug.check(sign.check(Type.DEFINITION_SIGN), "The object is not a definition sign!");
        Bug.check(expression.check(Type.EXPRESSION), "The object is not an expression!");
        return new Syntax(Type.DEFINITION_STATEMENT, name, sign, expression);
    }

    /** Syntax object of a definition sign. */
    public static Syntax ofDefinitionSign(Token sign) {
        Bug.check(sign.check(Token.Type.EQUAL), "The token is not a definition sign!");
        return new Syntax(Type.DEFINITION_SIGN, sign);
    }

    /** Syntax object of an expression. */
    public static Syntax ofExpression(Syntax body) {
        Bug.check(body.check(Type.BINARY_EXPRESSION, Type.UNARY_EXPRESSION, Type.REFERENCE, Type.LITERAL),
                "The object is not an expression body!");
        return new Syntax(Type.EXPRESSION, body);
    }

    /** Syntax object of a binary expression. */
    public static Syntax ofBinaryExpression(Syntax left, Syntax operator, Syntax right) {
        Bug.check(left.check(Type.EXPRESSION), "The object is not an expression!");
        Bug.check(operator.check(Type.BINARY_OPERATOR), "The object is not a binary operator!");
        Bug.check(right.check(Type.EXPRESSION), "The object is not an expression!");
        return new Syntax(Type.BINARY_EXPRESSION, left, operator, right);
    }

    /** Syntax object of a binary operator. */
    public static Syntax ofBinaryOperator(Token operator) {
        Bug.check(operator.check(Token.Type.PLUS, Token.Type.MINUS, Token.Type.STAR, Token.Type.FORWARD_SLASH,
                Token.Type.PERCENT), "The token is not a binary operator!");
        return new Syntax(Type.BINARY_OPERATOR, operator);
    }

    /** Syntax object of a unary expression. */
    public static Syntax ofUnaryExpression(Syntax operator, Syntax expression) {
        Bug.check(operator.check(Type.UNARY_OPERATOR), "The object is not a unary operator!");
        Bug.check(expression.check(Type.EXPRESSION), "The object is not an expression!");
        return new Syntax(Type.UNARY_EXPRESSION, operator, expression);
    }

    /** Syntax object of a unary operator. */
    public static Syntax ofUnaryOperator(Token operator) {
        Bug.check(operator.check(Token.Type.PLUS, Token.Type.MINUS), "The token is not a unary operator!");
        return new Syntax(Type.UNARY_OPERATOR, operator);
    }

    /** Syntax object of a reference. */
    public static Syntax ofReference(Token reference) {
        Bug.check(reference.check(Token.Type.IDENTIFIER), "The token is not a reference!");
        return new Syntax(Type.REFERENCE, reference);
    }

    /** Syntax object of a literal token. */
    public static Syntax ofLiteral(Token literal) {
        Bug.check(literal.check(Token.Type.NUMBER), "The token is not a literal!");
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

    /** Whether the type of the syntax object is one of the given types. */
    public boolean check(Type... types) {
        for (Type type : types) {
            if (this.type.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
