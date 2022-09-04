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
import jthrice.utility.Maybe;
import jthrice.utility.None;
import jthrice.utility.Some;

/** Parses a list of lexemes to a program node. */
public final class Parser {
  /** Parse the source in the given resolution. */
  public static Maybe<Node.Program> parse(Resolution resolution) {
    var parser = new Parser(resolution, Lexer.lex(resolution));
    return parser.parse();
  }

  /** Resolution of the parsed lexemes. */
  private final Resolution        resolution;
  /** Current lexeme to be parsed. */
  private Maybe<Iterator<Lexeme>> cursor;

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
  private <T extends Lexeme> Maybe<T> consume(Class<? extends T>... types) {
    if (this.cursor.not()) {
      return None.of();
    }
    var token = this.cursor.get().cast(types);
    if (token.is()) {
      this.next();
    }
    return token;
  }

  /** Parse the program. */
  private Maybe<Node.Program> parse() {
    Bug.check(this.cursor.is(), "There is no EOF!");
    var statements = new ArrayList<Node.Statement>();
    while (true) {
      var statement = this.parseStatement();
      if (statement.not()) {
        break;
      }
      statements.add(statement.get());
    }
    Bug.check(this.cursor.is(), "There is no EOF!");
    var eof = this.consume(Lexeme.EOF.class);
    if (eof.not()) {
      this.error("Expected the end of the file!");
      return None.of();
    }
    Bug.check(this.cursor.not(), "There are lexemes after the EOF!");
    return Some.of(new Node.Program(List.of(statements), eof.get()));
  }

  /** Parse a statement. */
  private Maybe<Node.Statement> parseStatement() {
    return this.parseDefinition();
  }

  /** Parse a definition. */
  private Maybe<Node.Statement> parseDefinition() {
    var name = this.consume(Lexeme.Identifier.class);
    if (name.not()) {
      return None.of();
    }
    var separator = this.consume(Lexeme.Colon.class);
    if (separator.not()) {
      this.error(
        "Expected a `:` at the definition of `" + name.get().portion + "`!");
      return None.of();
    }
    var type = this.parseExpression();
    if (type.not()) {
      this.error(
        "Expected the type at the definition of `" + name.get().portion + "`!");
      return None.of();
    }
    var assignment = this.consume(Lexeme.Equal.class);
    if (assignment.not()) {
      this.error(
        "Expected a `=` at the definition of `" + name.get().portion + "`!");
      return None.of();
    }
    var value = this.parseExpression();
    if (type.not()) {
      this.error("Expected the value at the definition of `"
        + name.get().portion + "`!");
      return None.of();
    }
    var end = this.consume(Lexeme.Semicolon.class);
    if (end.not()) {
      this.error(
        "Expected a `;` at the definition of `" + name.get().portion + "`!");
      return None.of();
    }
    return Some.of(new Node.Definition(name.get(), separator.get(), type.get(),
      assignment.get(), value.get(), end.get()));
  }

  /** Parse an expression. */
  private Maybe<Node.Expression> parseExpression() {
    return this.parseTerm();
  }

  /** Parse a term. */
  private Maybe<Node.Expression> parseTerm() {
    return this.parseBinary(this::parseFactor, Lexeme.Plus.class,
      Lexeme.Minus.class);
  }

  /** Parse a factor. */
  private Maybe<Node.Expression> parseFactor() {
    return this.parseBinary(this::parseUnary, Lexeme.Star.class,
      Lexeme.ForwardSlash.class, Lexeme.Percent.class);
  }

  /** Parse a binary. */
  @SafeVarargs
  private Maybe<Node.Expression> parseBinary(
    Supplier<Maybe<Node.Expression>> operand,
    Class<? extends Lexeme.Token>... types) {
    var left = operand.get();
    if (left.not()) {
      return None.of();
    }
    var binary = left.get();
    while (true) {
      var operator = this.consume(types);
      if (operator.not()) {
        break;
      }
      var right = operand.get();
      if (right.not()) {
        this.error(
          "Expected the right hand side in the expression after the operator `"
            + operator.get().portion + "`!");
        return None.of();
      }
      binary = new Node.Binary(operator.get(), binary, right.get());
    }
    return Some.of(binary);
  }

  /** Parse a unary. */
  private Maybe<Node.Expression> parseUnary() {
    var operator = this.consume(Lexeme.Plus.class, Lexeme.Minus.class);
    if (operator.not()) {
      return this.parseGroup();
    }
    var operand = this.parseGroup();
    if (operand.not()) {
      this.error("Expected the operand in the expression after the operator `"
        + operator.get().portion + "`!");
      return None.of();
    }
    return Some.of(new Node.Unary(operator.get(), operand.get()));
  }

  /** Parse a group. */
  private Maybe<Node.Expression> parseGroup() {
    var opening = this.consume(Lexeme.OpeningParentheses.class);
    if (opening.not()) {
      return this.parsePrimary();
    }
    var elevated = this.parseExpression();
    if (elevated.not()) {
      this.error("Expected an expression after `(`!");
      return None.of();
    }
    var closing = this.consume(Lexeme.ClosingParentheses.class);
    if (closing.not()) {
      this.error("Expected `)` at the end of the expression!");
    }
    return Some
      .of(new Node.Group(elevated.get(), opening.get(), closing.get()));
  }

  /** Parse a primary. */
  private Maybe<Node.Expression> parsePrimary() {
    var name = this.consume(Lexeme.Identifier.class);
    if (name.is()) {
      return Some.of(new Node.Access(name.get()));
    }
    var value = this.consume(Lexeme.class, Lexeme.I1.class, Lexeme.I2.class,
      Lexeme.I4.class, Lexeme.I8.class, Lexeme.IX.class, Lexeme.U1.class,
      Lexeme.U2.class, Lexeme.U4.class, Lexeme.U8.class, Lexeme.UX.class,
      Lexeme.F4.class, Lexeme.F8.class, Lexeme.Rinf.class);
    return Some.of(new Node.Literal(value.get()));
  }
}
