// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;
import java.util.function.Supplier;

import jthrice.launcher.Resolution;
import jthrice.lexer.Lexer;
import jthrice.lexer.Lexeme;
import jthrice.utility.Bug;
import jthrice.utility.Iterator;
import jthrice.utility.List;
import jthrice.utility.Result;

/** Parses a list of lexemes to a program node. */
public class Parser {
    /** Parse the source in the given resolution. */
    public static Result<Node.Program> parse(Resolution resolution) {
        var parser = new Parser(resolution, Lexer.lex(resolution));
        return parser.parse();
    }

    /** Resolution of the parsed lexemes. */
    private final Resolution resolution;
    /** Current lexeme to be parsed. */
    private Result<Iterator<Lexeme>> cursor;

    private Parser(Resolution resolution, List<Lexeme> tokens) {
        this.resolution = resolution;
        cursor = Iterator.ofFirst(tokens);
    }

    /** Give an error to the resolution about the current lexeme. */
    private void error(String message) {
        resolution.error("PARSER", cursor.get().get().portion, message);
    }

    /** Move to the next lexeme. */
    private void next() {
        cursor = cursor.get().next();
    }

    /**
     * Get the current lexeme if it exists and its of one of the given types.
     * Consume the lexeme if its returned.
     */
    @SafeVarargs
    private <T extends Lexeme> Result<T> consume(Class<? extends T>... types) {
        if (cursor.empty()) {
            return Result.ofUnexisting();
        }
        var token = cursor.get().cast(types);
        if (token.valid()) {
            next();
        }
        return token;
    }

    /** Parse the program. */
    private Result<Node.Program> parse() {
        Bug.check(cursor.valid(), "There is no EOF!");
        var statements = new ArrayList<Node.Statement>();
        while (true) {
            var statement = parseStatement();
            if (statement.empty()) {
                break;
            }
            statements.add(statement.get());
        }
        Bug.check(cursor.valid(), "There is no EOF!");
        var eof = consume(Lexeme.Token.EOF.class);
        if (eof.empty()) {
            error("Expected the end of the file!");
            return Result.ofUnexisting();
        }
        Bug.check(cursor.empty(), "There are lexemes after the EOF!");
        return Result.of(new Node.Program(new List<>(statements), eof.get()));
    }

    /** Parse a statement. */
    private Result<Node.Statement> parseStatement() {
        return parseDefinition();
    }

    /** Parse a definition. */
    private Result<Node.Statement> parseDefinition() {
        var name = consume(Lexeme.Identifier.class);
        if (name.empty()) {
            return Result.ofUnexisting();
        }
        var separator = consume(Lexeme.Token.Colon.class);
        if (separator.empty()) {
            error("Expected a `:` at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        var type = parseExpression();
        if (type.empty()) {
            error("Expected the type at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        var assignment = consume(Lexeme.Token.Equal.class);
        if (assignment.empty()) {
            error("Expected a `=` at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        var value = parseExpression();
        if (type.empty()) {
            error("Expected the value at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        var end = consume(Lexeme.Token.Semicolon.class);
        if (end.empty()) {
            error("Expected a `;` at the definition of `" + name.get().portion + "`!");
            return Result.ofUnexisting();
        }
        return Result.of(new Node.Statement.Definition(name.get(), separator.get(), type.get(), assignment.get(),
                value.get(), end.get()));
    }

    /** Parse an expression. */
    private Result<Node.Expression> parseExpression() {
        return parseTerm();
    }

    /** Parse a term. */
    private Result<Node.Expression> parseTerm() {
        return parseBinary(this::parseFactor, Lexeme.Token.Plus.class, Lexeme.Token.Minus.class);
    }

    /** Parse a factor. */
    private Result<Node.Expression> parseFactor() {
        return parseBinary(this::parseUnary, Lexeme.Token.Star.class, Lexeme.Token.ForwardSlash.class,
                Lexeme.Token.Percent.class);
    }

    /** Parse a binary. */
    @SafeVarargs
    private Result<Node.Expression> parseBinary(Supplier<Result<Node.Expression>> operand,
            Class<? extends Lexeme.Token>... types) {
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
            binary = new Node.Expression.Binary(operator.get(), binary, right.get());
        }
        return Result.of(binary);
    }

    /** Parse a unary. */
    private Result<Node.Expression> parseUnary() {
        var operator = consume(Lexeme.Token.Plus.class, Lexeme.Token.Minus.class);
        if (operator.empty()) {
            return parseGroup();
        }
        var operand = parseGroup();
        if (operand.empty()) {
            error("Expected the operand in the expression after the operator `" + operator.get().portion + "`!");
            return Result.ofUnexisting();
        }
        return Result.of(new Node.Expression.Unary(operator.get(), operand.get()));
    }

    /** Parse a group. */
    private Result<Node.Expression> parseGroup() {
        var opening = consume(Lexeme.Token.OpeningParentheses.class);
        if (opening.empty()) {
            return parsePrimary();
        }
        var elevated = parseExpression();
        if (elevated.empty()) {
            error("Expected an expression after `(`!");
            return Result.ofUnexisting();
        }
        var closing = consume(Lexeme.Token.ClosingParentheses.class);
        if (closing.empty()) {
            error("Expected `)` at the end of the expression!");
        }
        return Result.of(new Node.Expression.Group(elevated.get(), opening.get(), closing.get()));
    }

    /** Parse a primary. */
    private Result<Node.Expression> parsePrimary() {
        var name = consume(Lexeme.Identifier.class);
        if (name.valid()) {
            return Result.of(new Node.Expression.Primary.Access(name.get()));
        }
        var value = consume(Lexeme.Number.class, Lexeme.Keyword.I1.class, Lexeme.Keyword.I2.class,
                Lexeme.Keyword.I4.class,
                Lexeme.Keyword.I8.class, Lexeme.Keyword.IX.class, Lexeme.Keyword.U1.class, Lexeme.Keyword.U2.class,
                Lexeme.Keyword.U4.class, Lexeme.Keyword.U8.class, Lexeme.Keyword.UX.class, Lexeme.Keyword.F4.class,
                Lexeme.Keyword.F8.class);
        return Result.of(new Node.Expression.Primary.Literal(value.get()));
    }
}
