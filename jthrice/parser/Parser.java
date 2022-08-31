// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

import jthrice.Bug;
import jthrice.launcher.Resolution;
import jthrice.lexer.Lexer;
import jthrice.lexer.Token;

/** Parses a list of tokens to syntatic symbols. */
public class Parser {
    /** Parse the source in the given resolution. */
    public static Optional<Syntatic.Source> parse(Resolution resolution) {
        var parser = new Parser(resolution, Lexer.lex(resolution));
        return parser.parse();
    }

    /** Resolution of the parsed tokens. */
    private final Resolution resolution;
    /** Current token to be parsed. */
    private Optional<Location> cursor;

    private Parser(Resolution resolution, Token[] tokens) {
        this.resolution = resolution;
        cursor = Location.ofFirst(tokens);
    }

    /** Give an error to the resolution about the current token. */
    private void error(String message) {
        resolution.error("PARSER", cursor.get().get().portion, message);
    }

    /** Move to the next token. */
    private void next() {
        cursor = cursor.get().next();
    }

    /**
     * Get the current token if it exists and its of one of the given types. Consume
     * the token if its returned.
     */
    @SafeVarargs
    private <T extends Token> Optional<T> consume(Class<? extends T>... types) {
        if (cursor.isEmpty()) {
            return Optional.empty();
        }
        var token = cursor.get().cast(types);
        if (token.isPresent()) {
            next();
        }
        return token;
    }

    /** Parse a source file. */
    private Optional<Syntatic.Source> parse() {
        Bug.check(cursor.isPresent(), "There is no EOF!");
        var statements = new ArrayList<Syntatic.Statement>();
        while (true) {
            var statement = parseStatement();
            if (statement.isEmpty()) {
                break;
            }
            statements.add(statement.get());
        }
        Bug.check(cursor.isPresent(), "There is no EOF!");
        var eof = consume(Token.Mark.EOF.class);
        if (eof.isEmpty()) {
            error("Expected the end of the file!");
            return Optional.empty();
        }
        Bug.check(cursor.isEmpty(), "There are tokens after the EOF!");
        return Optional.of(new Syntatic.Source(statements.toArray(new Syntatic.Statement[0]), eof.get()));
    }

    /** Parse a statement. */
    private Optional<Syntatic.Statement> parseStatement() {
        return parseDefinition();
    }

    /** Parse a definition. */
    private Optional<Syntatic.Statement> parseDefinition() {
        var name = consume(Token.Identifier.class);
        if (name.isEmpty()) {
            return Optional.empty();
        }
        var separator = consume(Token.Mark.Colon.class);
        if (separator.isEmpty()) {
            error("Expected a `:` at the definition of `" + name.get().portion + "`!");
            return Optional.empty();
        }
        var type = parseExpression();
        if (type.isEmpty()) {
            error("Expected the type at the definition of `" + name.get().portion + "`!");
            return Optional.empty();
        }
        var assignment = consume(Token.Mark.Equal.class);
        if (assignment.isEmpty()) {
            error("Expected a `=` at the definition of `" + name.get().portion + "`!");
            return Optional.empty();
        }
        var value = parseExpression();
        if (type.isEmpty()) {
            error("Expected the value at the definition of `" + name.get().portion + "`!");
            return Optional.empty();
        }
        var end = consume(Token.Mark.Semicolon.class);
        if (end.isEmpty()) {
            error("Expected a `;` at the definition of `" + name.get().portion + "`!");
            return Optional.empty();
        }
        return Optional.of(new Syntatic.Statement.Definition(name.get(), separator.get(), type.get(), assignment.get(),
                value.get(), end.get()));
    }

    /** Parse an expression. */
    private Optional<Syntatic.Expression> parseExpression() {
        return parseTerm();
    }

    /** Parse a term. */
    private Optional<Syntatic.Expression> parseTerm() {
        return parseBinary(this::parseFactor, Token.Mark.Plus.class, Token.Mark.Minus.class);
    }

    /** Parse a factor. */
    private Optional<Syntatic.Expression> parseFactor() {
        return parseBinary(this::parseUnary, Token.Mark.Star.class, Token.Mark.ForwardSlash.class,
                Token.Mark.Percent.class);
    }

    /** Parse a binary. */
    @SafeVarargs
    private Optional<Syntatic.Expression> parseBinary(Supplier<Optional<Syntatic.Expression>> operand,
            Class<? extends Token.Mark>... types) {
        var left = operand.get();
        if (left.isEmpty()) {
            return Optional.empty();
        }
        Syntatic.Expression binary = left.get();
        while (true) {
            var operator = consume(types);
            if (operator.isEmpty()) {
                break;
            }
            var right = operand.get();
            if (right.isEmpty()) {
                error("Expected the right hand side in the expression after the operator `" + operator.get().portion
                        + "`!");
                return Optional.empty();
            }
            binary = new Syntatic.Expression.Binary(operator.get(), binary, right.get());
        }
        return Optional.of(binary);
    }

    /** Parse a unary. */
    private Optional<Syntatic.Expression> parseUnary() {
        var operator = consume(Token.Mark.Plus.class, Token.Mark.Minus.class);
        if (operator.isEmpty()) {
            return parseGroup();
        }
        var operand = parseGroup();
        if (operand.isEmpty()) {
            error("Expected the operand in the expression after the operator `" + operator.get().portion + "`!");
            return Optional.empty();
        }
        return Optional.of(new Syntatic.Expression.Unary(operator.get(), operand.get()));
    }

    /** Parse a group. */
    private Optional<Syntatic.Expression> parseGroup() {
        var opening = consume(Token.Mark.OpeningBracket.class);
        if (opening.isEmpty()) {
            return parsePrimary();
        }
        var elevated = parseExpression();
        if (elevated.isEmpty()) {
            error("Expected an expression after `(`!");
            return Optional.empty();
        }
        var closing = consume(Token.Mark.ClosingBracket.class);
        if (closing.isEmpty()) {
            error("Expected `)` at the end of the expression!");
        }
        return Optional.of(new Syntatic.Expression.Group(elevated.get(), opening.get(), closing.get()));
    }

    /** Parse a primary. */
    private Optional<Syntatic.Expression> parsePrimary() {
        var name = consume(Token.Identifier.class);
        if (name.isPresent()) {
            return Optional.of(new Syntatic.Expression.Primary.Access(name.get()));
        }
        var value = consume(Token.Number.class, Token.Keyword.I1.class, Token.Keyword.I2.class, Token.Keyword.I4.class,
                Token.Keyword.I8.class, Token.Keyword.IX.class, Token.Keyword.U1.class, Token.Keyword.U2.class,
                Token.Keyword.U4.class, Token.Keyword.U8.class, Token.Keyword.UX.class, Token.Keyword.F4.class,
                Token.Keyword.F8.class);
        return Optional.of(new Syntatic.Expression.Primary.Literal(value.get()));
    }
}
