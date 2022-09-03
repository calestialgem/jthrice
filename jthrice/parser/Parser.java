// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;
import java.util.function.Supplier;

import jthrice.launcher.Resolution;
import jthrice.lexer.Lexeme;
import jthrice.lexer.Lexer;
import jthrice.utility.Bug;
import jthrice.utility.Iterator;
import jthrice.utility.List;
import jthrice.utility.Result;

/** Parses a list of lexemes to a program node. */
public final class Parser {
  /** Parse the source in the given resolution. */
  public static Result<Node.Program> parse(Resolution resolution) {
    var parser = new Parser(resolution, Lexer.lex(resolution));
    return parser.parse();
  }

  /** Resolution of the parsed lexemes. */
  private final Resolution         resolution;
  /** Current lexeme to be parsed. */
  private Result<Iterator<Lexeme>> cursor;

  private Parser(Resolution resolution, List<Lexeme> tokens) {
    this.resolution = resolution;
    this.cursor     = Iterator.ofFirst(tokens);
  }

  /** Give an error to the resolution about the current lexeme. */
  private void error(String message) {
    this.resolution.error("PARSER", this.cursor.get().get().portion, message);
  }

  /** Move to the next lexeme. */
  private void next() {
    this.cursor = this.cursor.get().next();
  }

  /**
   * Get the current lexeme if it exists and its of one of the given types.
   * Consume the lexeme if its returned.
   */
  @SafeVarargs
  private <T extends Lexeme> Result<T> consume(Class<? extends T>... types) {
    if (this.cursor.empty()) {
      return Result.ofUnexisting();
    }
    var token = this.cursor.get().cast(types);
    if (token.valid()) {
      this.next();
    }
    return token;
  }

  /** Parse the program. */
  private Result<Node.Program> parse() {
    Bug.check(this.cursor.valid(), "There is no EOF!");
    var statements = new ArrayList<Node.Statement>();
    while (true) {
      var statement = this.parseStatement();
      if (statement.empty()) {
        break;
      }
      statements.add(statement.get());
    }
    Bug.check(this.cursor.valid(), "There is no EOF!");
    var eof = this.consume(Lexeme.EOF.class);
    if (eof.empty()) {
      this.error("Expected the end of the file!");
      return Result.ofUnexisting();
    }
    Bug.check(this.cursor.empty(), "There are lexemes after the EOF!");
    return Result.of(new Node.Program(new List<>(statements), eof.get()));
  }

  /** Parse a statement. */
  private Result<Node.Statement> parseStatement() {
    return this.parseDefinition();
  }

  /** Parse a definition. */
  private Result<Node.Statement> parseDefinition() {
    var name = this.consume(Lexeme.Identifier.class);
    if (name.empty()) {
      return Result.ofUnexisting();
    }
    var separator = this.consume(Lexeme.Colon.class);
    if (separator.empty()) {
      this.error(
        "Expected a `:` at the definition of `" + name.get().portion + "`!");
      return Result.ofUnexisting();
    }
    var type = this.parseExpression();
    if (type.empty()) {
      this.error(
        "Expected the type at the definition of `" + name.get().portion + "`!");
      return Result.ofUnexisting();
    }
    var assignment = this.consume(Lexeme.Equal.class);
    if (assignment.empty()) {
      this.error(
        "Expected a `=` at the definition of `" + name.get().portion + "`!");
      return Result.ofUnexisting();
    }
    var value = this.parseExpression();
    if (type.empty()) {
      this.error("Expected the value at the definition of `"
        + name.get().portion + "`!");
      return Result.ofUnexisting();
    }
    var end = this.consume(Lexeme.Semicolon.class);
    if (end.empty()) {
      this.error(
        "Expected a `;` at the definition of `" + name.get().portion + "`!");
      return Result.ofUnexisting();
    }
    return Result.of(new Node.Definition(name.get(), separator.get(),
      type.get(), assignment.get(), value.get(), end.get()));
  }

  /** Parse an expression. */
  private Result<Node.Expression> parseExpression() {
    return this.parseTerm();
  }

  /** Parse a term. */
  private Result<Node.Expression> parseTerm() {
    return this.parseBinary(this::parseFactor, Lexeme.Plus.class,
      Lexeme.Minus.class);
  }

  /** Parse a factor. */
  private Result<Node.Expression> parseFactor() {
    return this.parseBinary(this::parseUnary, Lexeme.Star.class,
      Lexeme.ForwardSlash.class, Lexeme.Percent.class);
  }

  /** Parse a binary. */
  @SafeVarargs
  private Result<Node.Expression> parseBinary(
    Supplier<Result<Node.Expression>> operand,
    Class<? extends Lexeme.Token>... types) {
    var left = operand.get();
    if (left.empty()) {
      return Result.ofUnexisting();
    }
    var binary = left.get();
    while (true) {
      var operator = this.consume(types);
      if (operator.empty()) {
        break;
      }
      var right = operand.get();
      if (right.empty()) {
        this.error(
          "Expected the right hand side in the expression after the operator `"
            + operator.get().portion + "`!");
        return Result.ofUnexisting();
      }
      binary = new Node.Binary(operator.get(), binary, right.get());
    }
    return Result.of(binary);
  }

  /** Parse a unary. */
  private Result<Node.Expression> parseUnary() {
    var operator = this.consume(Lexeme.Plus.class, Lexeme.Minus.class);
    if (operator.empty()) {
      return this.parseGroup();
    }
    var operand = this.parseGroup();
    if (operand.empty()) {
      this.error("Expected the operand in the expression after the operator `"
        + operator.get().portion + "`!");
      return Result.ofUnexisting();
    }
    return Result.of(new Node.Unary(operator.get(), operand.get()));
  }

  /** Parse a group. */
  private Result<Node.Expression> parseGroup() {
    var opening = this.consume(Lexeme.OpeningParentheses.class);
    if (opening.empty()) {
      return this.parsePrimary();
    }
    var elevated = this.parseExpression();
    if (elevated.empty()) {
      this.error("Expected an expression after `(`!");
      return Result.ofUnexisting();
    }
    var closing = this.consume(Lexeme.ClosingParentheses.class);
    if (closing.empty()) {
      this.error("Expected `)` at the end of the expression!");
    }
    return Result
      .of(new Node.Group(elevated.get(), opening.get(), closing.get()));
  }

  /** Parse a primary. */
  private Result<Node.Expression> parsePrimary() {
    var name = this.consume(Lexeme.Identifier.class);
    if (name.valid()) {
      return Result.of(new Node.Access(name.get()));
    }
    var value = this.consume(Lexeme.class, Lexeme.I1.class, Lexeme.I2.class,
      Lexeme.I4.class, Lexeme.I8.class, Lexeme.IX.class, Lexeme.U1.class,
      Lexeme.U2.class, Lexeme.U4.class, Lexeme.U8.class, Lexeme.UX.class,
      Lexeme.F4.class, Lexeme.F8.class);
    return Result.of(new Node.Literal(value.get()));
  }
}
