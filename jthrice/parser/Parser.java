// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.*;

import jthrice.launcher.*;
import jthrice.lexer.*;

/** Parses a list of lexemes to a program node. */
public final class Parser {
  /** Parse the given lexemes and report to the given resolution. */
  public static Node.Program parse(Resolution resolution,
    List<Lexeme> lexemes) {
    var statements = new ArrayList<Node.Statement>();
    var cursor     = Cursor.of(lexemes);
    while (cursor.has()) {
      var statement = Parser.parseStatement(resolution, cursor);
      if (statement == null) {
        break;
      }
      statements.add(statement);
    }
    if (!cursor.has()) {
      resolution.error("PARSER",
        "There is no EOF token at the end of the file!");
      return null;
    }
    var lexeme = cursor.next();
    if (cursor.consume().has()) {
      var remaining = Portion.of(lexeme.portion, cursor.next().portion);
      while (cursor.consume().has()) {
        remaining = Portion.of(remaining, cursor.next().portion);
      }
      resolution.error("PARSER", remaining,
        "There are tokens that could not be parsed!");
      return null;
    }
    if (!(lexeme instanceof Lexeme.EOF eof)) {
      resolution.error("PARSER", lexeme.portion,
        "File ends without a EOF token!");
      return null;
    }
    if (statements.isEmpty()) {
      resolution.error("PARSER", "There are no statements in the file!");
      return null;
    }
    return new Node.Program(statements, eof);
  }

  /** Try to parse a statement node at the given cursor and report to the given
   * resolution. */
  private static Node.Statement parseStatement(Resolution resolution,
    Cursor cursor) {
    return Parser.parseDefinition(resolution, cursor);
  }

  /** Try to parse a definition node at the given cursor and report to the given
   * resolution. */
  private static Node.Definition parseDefinition(Resolution resolution,
    Cursor cursor) {
    if (!(cursor.next() instanceof Lexeme.Identifier name)) {
      return null;
    }

    if (!cursor.consume().has()) {
      resolution.error("PARSER", name.portion,
        "There is no `:` after the name in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }
    if (!(cursor.next() instanceof Lexeme.Colon separator)) {
      resolution.error("PARSER", cursor.next().portion,
        "Expected a `:` after the name in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }

    if (!cursor.consume().has()) {
      resolution.error("PARSER", separator.portion,
        "There is no type after the `:` in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }
    var type = Parser.parseExpression(resolution, cursor);
    if (type == null) {
      resolution.error("PARSER", cursor.next().portion,
        "Expected a type after the `:` in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }

    if (!cursor.has()) {
      resolution.error("PARSER", type.portion,
        "There is no `=` after the type in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }
    if (!(cursor.next() instanceof Lexeme.Equal assignment)) {
      resolution.error("PARSER", cursor.next().portion,
        "Expected a `=` after the type in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }

    if (!cursor.consume().has()) {
      resolution.error("PARSER", assignment.portion,
        "There is no value after the `=` in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }
    var value = Parser.parseExpression(resolution, cursor);
    if (value == null) {
      resolution.error("PARSER", cursor.next().portion,
        "Expected a value after the `=` in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }

    if (!cursor.has()) {
      resolution.error("PARSER", value.portion,
        "There is no `;` after the value in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }
    if (!(cursor.next() instanceof Lexeme.Semicolon end)) {
      resolution.error("PARSER", cursor.next().portion,
        "Expected a `;` after the value in the definition of `%s`!"
          .formatted(name.value));
      return null;
    }
    cursor.consume();

    return new Node.Definition(name, separator, type, assignment, value, end);
  }

  /** Try to parse an expression node at the given cursor and report to the
   * given resolution. */
  private static Node.Expression parseExpression(Resolution resolution,
    Cursor cursor) {
    return Parser.parseTermExpression(resolution, cursor);
  }

  /** Try to parse a term expression node at the given cursor and report to the
   * given resolution. */
  private static Node.Expression parseTermExpression(Resolution resolution,
    Cursor cursor) {
    var left = Parser.parseFactorExpression(resolution, cursor);
    if (left == null) {
      return null;
    }
    loop: while (cursor.has()) {
      var lexeme = cursor.next();
      if (!(lexeme instanceof Lexeme.Token operator)) {
        break;
      }
      switch (operator) {
        case Lexeme.Plus plus:
          break;
        case Lexeme.Minus minus:
          break;
        default:
          break loop;
      }

      if (!cursor.consume().has()) {
        resolution.error("PARSER", operator.portion,
          "There is no right hand side after the `%s` in the binary operator!"
            .formatted(operator));
        return null;
      }
      var right = Parser.parseFactorExpression(resolution, cursor);
      if (right == null) {
        resolution.error("PARSER", cursor.next().portion,
          "Expected a right hand side after the `%s` in the binary operator!"
            .formatted(operator));
        return null;
      }

      left = new Node.Binary(operator, left, right);
    }
    return left;
  }

  /** Try to parse a factor expression node at the given cursor and report to
   * the given resolution. */
  private static Node.Expression parseFactorExpression(Resolution resolution,
    Cursor cursor) {
    var left = Parser.parseUnary(resolution, cursor);
    if (left == null) {
      return null;
    }
    loop: while (cursor.has()) {
      var lexeme = cursor.next();
      if (!(lexeme instanceof Lexeme.Token operator)) {
        break;
      }
      switch (operator) {
        case Lexeme.Star star:
          break;
        case Lexeme.ForwardSlash forwardSlash:
          break;
        case Lexeme.Percent percent:
          break;
        default:
          break loop;
      }

      if (!cursor.consume().has()) {
        resolution.error("PARSER", operator.portion,
          "There is no right hand side after the `%s` in the binary operator!"
            .formatted(operator));
        return null;
      }
      var right = Parser.parseUnary(resolution, cursor);
      if (right == null) {
        resolution.error("PARSER", cursor.next().portion,
          "Expected a right hand side after the `%s` in the binary operator!"
            .formatted(operator));
        return null;
      }

      left = new Node.Binary(operator, left, right);
    }
    return left;
  }

  /** Try to parse a unary node at the given cursor and report to the given
   * resolution. */
  private static Node.Expression parseUnary(Resolution resolution,
    Cursor cursor) {
    if (!(cursor.next() instanceof Lexeme.Token operator)) {
      return parseGroup(resolution, cursor);
    }
    switch (operator) {
      case Lexeme.Plus plus:
        break;
      case Lexeme.Minus minus:
        break;
      default:
        return parseGroup(resolution, cursor);
    }

    if (!cursor.consume().has()) {
      resolution.error("PARSER", operator.portion,
        "There is no operand after the `%s` in the unary operator!"
          .formatted(operator));
      return null;
    }
    var operand = Parser.parseGroup(resolution, cursor);
    if (operand == null) {
      resolution.error("PARSER", cursor.next().portion,
        "Expected an operand after the `%s` in the unary operator!"
          .formatted(operator));
      return null;
    }

    return new Node.Unary(operator, operand);
  }

  /** Try to parse a group node at the given cursor and report to the given
   * resolution. */
  private static Node.Expression parseGroup(Resolution resolution,
    Cursor cursor) {
    if (!(cursor.next() instanceof Lexeme.OpeningParentheses opening)) {
      return Parser.parsePrimary(cursor);
    }

    if (!cursor.consume().has()) {
      resolution.error("PARSER", opening.portion,
        "There is no expression after the `(` in the group!");
      return null;
    }
    var elevated = Parser.parseExpression(resolution, cursor);
    if (elevated == null) {
      resolution.error("PARSER", cursor.next().portion,
        "Expected an expression after the `(` in the group!");
      return null;
    }

    if (!cursor.has()) {
      resolution.error("PARSER", elevated.portion,
        "There is no `)` after the expression in the group!");
      return null;
    }
    if (!(cursor.next() instanceof Lexeme.ClosingParentheses closing)) {
      resolution.error("PARSER", cursor.next().portion,
        "Expected a `)` after the expression in the group!");
      return null;
    }
    cursor.consume();

    return new Node.Group(elevated, opening, closing);
  }

  /** Try to parse a primary expression node at the given cursor and report to
   * the given resolution. */
  private static Node.Expression parsePrimary(Cursor cursor) {
    var result = switch (cursor.next()) {
      case Lexeme.I1 i1 -> new Node.Literal(i1);
      case Lexeme.I2 i2 -> new Node.Literal(i2);
      case Lexeme.I4 i4 -> new Node.Literal(i4);
      case Lexeme.I8 i8 -> new Node.Literal(i8);
      case Lexeme.Ix ix -> new Node.Literal(ix);
      case Lexeme.U1 u1 -> new Node.Literal(u1);
      case Lexeme.U2 u2 -> new Node.Literal(u2);
      case Lexeme.U4 u4 -> new Node.Literal(u4);
      case Lexeme.U8 u8 -> new Node.Literal(u8);
      case Lexeme.Ux ux -> new Node.Literal(ux);
      case Lexeme.F4 f4 -> new Node.Literal(f4);
      case Lexeme.F8 f8 -> new Node.Literal(f8);
      case Lexeme.Rinf rinf -> new Node.Literal(rinf);
      case Lexeme.Number number -> new Node.Literal(number);
      case Lexeme.Identifier name -> new Node.Access(name);
      default -> null;
    };
    if (result != null) {
      cursor.consume();
    }
    return result;
  }
}
