// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;
import java.util.function.Supplier;

import jthrice.launcher.Resolution;
import jthrice.lexer.Lexer;
import jthrice.lexer.Token;
import jthrice.utility.Bug;
import jthrice.utility.Iterator;
import jthrice.utility.List;
import jthrice.utility.Result;

/** Parses a list of tokens to a syntatic entity. */
public class Parser {
    /** Parse the source in the given resolution. */
    public static Result<Syntatic.Source> parse(Resolution resolution) {
        var parser = new Parser(resolution, Lexer.lex(resolution));
        return parser.parse();
    }

    /** Resolution of the parsed tokens. */
    private final Resolution resolution;
    /** Current token to be parsed. */
    private Result<Iterator<Token>> cursor;

    private Parser(Resolution resolution, List<Token> tokens) {
        this.resolution = resolution;
        cursor = Iterator.ofFirst(tokens);
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
    private <T extends Token> Result<T> consume(Class<? extends T>... types) {
        if (cursor.empty()) {
            return Result.ofUnexisting();
        }
        var token = cursor.get().cast(types);
        if (token.valid()) {
            next();
        }
        return token;
    }

    /** Parse the token list. */
    private Result<Syntatic.Source> parse() {
        Bug.check(cursor.valid(), "There is no EOF!");
        var statements = new ArrayList<Syntatic.Statement>();
        while (true) {
            var statement = parseStatement();
            if (statement.empty()) {
                break;
            }
            statements.add(statement.get());
        }
        Bug.check(cursor.valid(), "There is no EOF!");
        var eof = consume(Token.Mark.EOF.class);
        if (eof.empty()) {
            error("Expected the end of the file!");
            return Result.ofUnexisting();
        }
        Bug.check(cursor.empty(), "There are tokens after the EOF!");
        return Result.of(new Syntatic.Source(new List<>(statements), eof.get()));
    }

    /** Parse a statement. */
    private Result<Syntatic.Statement> parseStatement() {
        return parseDefinition();
    }

    /** Parse a definition. */
    private Result<Syntatic.Statement> parseDefinition() {
        var name = consume(Token.Identifier.class);
        if (name.empty()) {
            return Result.ofUnexisting();
        }
        var separator = consume(Token.Mark.Colon.class);
        if (separator.empty()) {
            error("Expected a `:` at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        var type = parseExpression();
        if (type.empty()) {
            error("Expected the type at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        var assignment = consume(Token.Mark.Equal.class);
        if (assignment.empty()) {
            error("Expected a `=` at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        var value = parseExpression();
        if (type.empty()) {
            error("Expected the value at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        var end = consume(Token.Mark.Semicolon.class);
        if (end.empty()) {
            error("Expected a `;` at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        return Result.of(new Syntatic.Statement.Definition(name.get(), separator.get(), type.get(), assignment.get(),
                value.get(), end.get()));
    }

    /** Parse an expression. */
    private Result<Syntatic.Expression> parseExpression() {
        return parseTerm();
    }

    /** Parse a term. */
    private Result<Syntatic.Expression> parseTerm() {
        return parseBinary(this::parseFactor, Token.Mark.Plus.class, Token.Mark.Minus.class);
    }

    /** Parse a factor. */
    private Result<Syntatic.Expression> parseFactor() {
        return parseBinary(this::parseUnary, Token.Mark.Star.class, Token.Mark.ForwardSlash.class,
                Token.Mark.Percent.class);
    }

    /** Parse a binary. */
    @SafeVarargs
    private Result<Syntatic.Expression> parseBinary(Supplier<Result<Syntatic.Expression>> operand,
            Class<? extends Token.Mark>... types) {
        var left = operand.get();
        if (left.empty()) {
            return Result.ofUnexisting();
        }
        var binary = left.get();
        while (true) {
            var operator = consume(types);
            if (operator.empty()) {
                break;
            }
            var right = operand.get();
            if (right.empty()) {
                error("Expected the right hand side in the expression after the operator `" + operator.get().portion
                        + "`!");
                return Result.ofUnexisting();
            }
            binary = new Syntatic.Expression.Binary(operator.get(), binary, right.get());
        }
        return Result.of(binary);
    }

    /** Parse a unary. */
    private Result<Syntatic.Expression> parseUnary() {
        var operator = consume(Token.Mark.Plus.class, Token.Mark.Minus.class);
        if (operator.empty()) {
            return parseGroup();
        }
        var operand = parseGroup();
        if (operand.empty()) {
            error("Expected the operand in the expression after the operator `" + operator.get().portion + "`!");
            return Result.ofUnexisting();
        }
        return Result.of(new Syntatic.Expression.Unary(operator.get(), operand.get()));
    }

    /** Parse a group. */
    private Result<Syntatic.Expression> parseGroup() {
        var opening = consume(Token.Mark.OpeningBracket.class);
        if (opening.empty()) {
            return parsePrimary();
        }
        var elevated = parseExpression();
        if (elevated.empty()) {
            error("Expected an expression after `(`!");
            return Result.ofUnexisting();
        }
        var closing = consume(Token.Mark.ClosingBracket.class);
        if (closing.empty()) {
            error("Expected `)` at the end of the expression!");
        }
        return Result.of(new Syntatic.Expression.Group(elevated.get(), opening.get(), closing.get()));
    }

    /** Parse a primary. */
    private Result<Syntatic.Expression> parsePrimary() {
        var name = consume(Token.Identifier.class);
        if (name.valid()) {
            return Result.of(new Syntatic.Expression.Primary.Access(name.get()));
        }
        var value = consume(Token.Number.class, Token.Keyword.I1.class, Token.Keyword.I2.class, Token.Keyword.I4.class,
                Token.Keyword.I8.class, Token.Keyword.IX.class, Token.Keyword.U1.class, Token.Keyword.U2.class,
                Token.Keyword.U4.class, Token.Keyword.U8.class, Token.Keyword.UX.class, Token.Keyword.F4.class,
                Token.Keyword.F8.class);
        return Result.of(new Syntatic.Expression.Primary.Literal(value.get()));
    }
}
