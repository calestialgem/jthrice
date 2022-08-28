// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;
import java.util.Optional;

import jthrice.Bug;
import jthrice.launcher.Resolution;
import jthrice.lexer.Lexer;
import jthrice.lexer.Token;

/** Parses a list of tokens to a syntax tree. */
public class Parser {
    /** Parse the source in the given resolution. */
    public static Syntax parse(Resolution resolution) {
        Parser parser = new Parser(resolution, Lexer.lex(resolution));
        return parser.parse();
    }

    /** Resolution of the parsed tokens. */
    private final Resolution resolution;
    /** Tokens to be parsed. */
    private final ArrayList<Token> tokens;
    /** Current index in the tokens. */
    private int index;

    private Parser(Resolution resolution, ArrayList<Token> tokens) {
        this.resolution = resolution;
        this.tokens = tokens;
        index = 0;
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

    /** Parse a source file syntax object. */
    private Syntax parse() {
        ArrayList<Syntax> statements = new ArrayList<>();
        while (has()) {
            Optional<Syntax> statement = parseStatement();
            if (statement.isEmpty()) {
                resolution.error("PARSER", current().portion, "Could not parse the token!");
                index++;
            } else {
                statements.add(statement.get());
            }
        }
        return Syntax.ofSource(statements.toArray(new Syntax[0]));
    }

    /** Parse a statement. */
    private Optional<Syntax> parseStatement() {
        Optional<Syntax> body = parseDefinitionStatement();
        if (body.isEmpty()) {
            return Optional.empty();
        }
        Optional<Syntax> separator = parseStatementSeparator();
        if (separator.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Syntax.ofStatement(body.get(), separator.get()));
    }

    /** Parse a statement separator. */
    private Optional<Syntax> parseStatementSeparator() {
        Token token = current();
        if (!has() || !token.check(Token.Type.SEMICOLON)) {
            return Optional.empty();
        }
        index++;
        return Optional.of(Syntax.ofStatementSeparator(token));
    }

    /** Parse a definition statement. */
    private Optional<Syntax> parseDefinitionStatement() {
        Optional<Syntax> name = parseReference();
        if (name.isEmpty()) {
            return Optional.empty();
        }
        Optional<Syntax> sign = parseDefinitionSign();
        if (sign.isEmpty()) {
            return Optional.empty();
        }
        Optional<Syntax> expression = parseExpression();
        if (expression.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Syntax.ofDefinitionStatement(name.get(), sign.get(), expression.get()));
    }

    /** Parse a definition sign. */
    private Optional<Syntax> parseDefinitionSign() {
        Token token = current();
        if (!has() || !token.check(Token.Type.EQUAL)) {
            return Optional.empty();
        }
        index++;
        return Optional.of(Syntax.ofDefinitionSign(token));
    }

    /** Parse an expression. */
    private Optional<Syntax> parseExpression() {
        Optional<Syntax> body = Optional.empty();
        body = body.or(this::parseLiteral).or(this::parseReference).or(this::parseUnaryExpression)
                .or(this::parseBinaryExpression);
        if (body.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Syntax.ofExpression(body.get()));
    }

    /** Parse a binary expression. */
    private Optional<Syntax> parseBinaryExpression() {
        Optional<Syntax> left = parseExpression();
        if (left.isEmpty()) {
            return Optional.empty();
        }
        Optional<Syntax> operator = parseBinaryOperator();
        if (operator.isEmpty()) {
            return Optional.empty();
        }
        Optional<Syntax> right = parseExpression();
        if (right.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Syntax.ofBinaryExpression(left.get(), operator.get(), right.get()));
    }

    /** Parse a binary operator. */
    private Optional<Syntax> parseBinaryOperator() {
        Token token = current();
        if (!has() || !token.check(Token.Type.PLUS, Token.Type.MINUS, Token.Type.STAR, Token.Type.FORWARD_SLASH,
                Token.Type.PERCENT)) {
            return Optional.empty();
        }
        index++;
        return Optional.of(Syntax.ofBinaryOperator(token));
    }

    /** Parse a unary expression. */
    private Optional<Syntax> parseUnaryExpression() {
        Optional<Syntax> operator = parseUnaryOperator();
        if (operator.isEmpty()) {
            return Optional.empty();
        }
        Optional<Syntax> expression = parseExpression();
        if (expression.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Syntax.ofUnaryExpression(operator.get(), expression.get()));
    }

    /** Parse a unary operator. */
    private Optional<Syntax> parseUnaryOperator() {
        Token token = current();
        if (!has() || !token.check(Token.Type.PLUS, Token.Type.MINUS)) {
            return Optional.empty();
        }
        index++;
        return Optional.of(Syntax.ofUnaryOperator(token));
    }

    /** Parse a reference. */
    private Optional<Syntax> parseReference() {
        Token token = current();
        if (!has() || !token.check(Token.Type.IDENTIFIER)) {
            return Optional.empty();
        }
        index++;
        return Optional.of(Syntax.ofReference(token));
    }

    /** Parse a literal. */
    private Optional<Syntax> parseLiteral() {
        Token token = current();
        if (!has() || !token.check(Token.Type.NUMBER)) {
            return Optional.empty();
        }
        index++;
        return Optional.of(Syntax.ofLiteral(token));
    }
}
