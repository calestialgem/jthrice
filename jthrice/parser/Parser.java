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
      while (cursor.consume().has()) {
      }
      resolution.error("PARSER",
        Portion.of(lexeme.portion, cursor.current().portion),
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
    return Node.ofProgram(statements, eof);
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
    var old  = cursor.next();
    var type = Parser.parseExpression(resolution, cursor, 0);
    if (type == null) {
      resolution.error("PARSER",
        Portion.of(old.portion, cursor.current().portion),
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
    old = cursor.next();
    var value = Parser.parseExpression(resolution, cursor, 0);
    if (value == null) {
      resolution.error("PARSER",
        Portion.of(old.portion, cursor.current().portion),
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

    return Node.ofDefinition(name, separator, type, assignment, value, end);
  }

  /** Try to parse an expression node at the given cursor and report to the
   * given resolution. */
  private static Node.Expression parseExpression(Resolution resolution,
    Cursor cursor, int precedence) {
    var first = cursor.next();
    for (var i = precedence; i < Operator.PRECEDENCE.size(); i++) {
      var expression = switch (Operator.PRECEDENCE.get(i)) {
        case Operator.Nofix nofix -> Parser.parseNofix(cursor, nofix);
        case Operator.Prefix prefix ->
          Parser.parsePrefix(resolution, cursor, prefix, i);
        case Operator.Postfix postfix ->
          Parser.parsePostfix(resolution, cursor, postfix, i);
        case Operator.Infix infix ->
          Parser.parseInfix(resolution, cursor, infix, i);
        case Operator.Outfix outfix ->
          Parser.parseOutfix(resolution, cursor, outfix);
        case Operator.Knitfix knitfix ->
          Parser.parseKnitfix(resolution, cursor, knitfix, i);
      };
      if (expression != null) {
        return expression;
      }
      if (!first.portion.equals(cursor.next().portion)) {
        return null;
      }
    }
    return null;
  }

  /** Try to parse the given nofix operator at the given cursor and report to
   * the given resolution. */
  private static Node.Nofix parseNofix(Cursor cursor, Operator.Nofix operator) {
    for (var type : operator.operands) {
      if (type.isInstance(cursor.next())) {
        var node = Node.ofNofix(cursor.next());
        cursor.consume();
        return node;
      }
    }
    return null;
  }

  /** Try to parse the given prefix operator at the given cursor and report to
   * the given resolution. */
  private static Node.Expression parsePrefix(Resolution resolution,
    Cursor cursor, Operator.Prefix operator, int precedence) {
    if (!operator.before.isInstance(cursor.next())) {
      return null;
    }
    var stack = new ArrayList<Lexeme>();
    do {
      stack.add(cursor.next());
    } while (cursor.consume().has()
      && operator.before.isInstance(cursor.next()));
    var before = stack.get(stack.size() - 1);

    if (!cursor.has()) {
      resolution.error("PARSER", before.portion,
        "There is no operand after the `%s` in the prefix operation!"
          .formatted(before));
      return null;
    }
    var old  = cursor.next();
    var last = Parser.parseExpression(resolution, cursor, precedence + 1);
    if (last == null) {
      resolution.error("PARSER",
        Portion.of(old.portion, cursor.current().portion),
        "Expected an operand after the `%s` in the prefix operation!"
          .formatted(before));
      return null;
    }

    for (var i = stack.size() - 1; i >= 0; i--) {
      last = Node.ofPrefix(stack.get(i), last);
    }
    return last;
  }

  /** Try to parse the given postfix operator at the given cursor and report to
   * the given resolution. */
  private static Node.Expression parsePostfix(Resolution resolution,
    Cursor cursor, Operator.Postfix operator, int precedence) {
    var first = Parser.parseExpression(resolution, cursor, precedence + 1);
    if (first == null) {
      return null;
    }

    while (true) {
      if (!cursor.has() || !operator.after.isInstance(cursor.next())) {
        break;
      }
      var after = cursor.next();
      cursor.consume();
      first = Node.ofPostfix(after, first);
    }

    return first;
  }

  /** Try to parse the given infix operator at the given cursor and report to
   * the given resolution. */
  private static Node.Expression parseInfix(Resolution resolution,
    Cursor cursor, Operator.Infix operator, int precedence) {
    var first = Parser.parseExpression(resolution, cursor, precedence + 1);
    if (first == null) {
      return null;
    }

    while (true) {
      if (!cursor.has() || !operator.between.isInstance(cursor.next())) {
        break;
      }
      var between = cursor.next();

      if (!cursor.consume().has()) {
        resolution.error("PARSER", between.portion,
          "There is no operand after the `%s` in the infix operation!"
            .formatted(between));
        return null;
      }
      var old  = cursor.next();
      var last = Parser.parseExpression(resolution, cursor, precedence + 1);
      if (last == null) {
        resolution.error("PARSER",
          Portion.of(old.portion, cursor.current().portion),
          "Expected an operand after the `%s` in the infix operation!"
            .formatted(between));
        return null;
      }

      first = Node.ofInfix(between, first, last);
    }

    return first;
  }

  /** Try to parse the given outfix operator at the given cursor and report to
   * the given resolution. */
  private static Node.Expression parseOutfix(Resolution resolution,
    Cursor cursor, Operator.Outfix operator) {
    if (!operator.before.isInstance(cursor.next())) {
      return null;
    }
    var before = cursor.next();

    if (!cursor.consume().has()) {
      resolution.error("PARSER", before.portion,
        "There is no operand after the `%s` in the outfix operation!"
          .formatted(before));
      return null;
    }
    var old    = cursor.next();
    var middle = Parser.parseExpression(resolution, cursor, 0);
    if (middle == null) {
      resolution.error("PARSER",
        Portion.of(old.portion, cursor.current().portion),
        "Expected an operand after the `%s` in the outfix operation!"
          .formatted(before));
      return null;
    }

    if (!cursor.has()) {
      resolution.error("PARSER", Portion.of(before.portion, middle.portion),
        "There is no matching `%s` for the `%s` in the outfix operation!"
          .formatted(operator.after, before));
      resolution.info("PARSER", before.portion,
        "Outfix operator is opened here.");
      return null;
    }
    if (!operator.after.isInstance(cursor.next())) {
      resolution.error("PARSER", cursor.next().portion,
        "Expected a matching `%s` for the `%s` in the outfix operation!"
          .formatted(operator.after, before));
      resolution.info("PARSER", before.portion,
        "Outfix operator is opened here.");
      return null;
    }
    middle = Node.ofOutfix(before, cursor.next(), middle);
    cursor.consume();
    return middle;
  }

  /** Try to parse the given knitfix operator at the given cursor and report to
   * the given resolution. */
  private static Node.Knitfix parseKnitfix(Resolution resolution, Cursor cursor,
    Operator.Knitfix operator, int precedence) {
    resolution.error("PARSER", "Not implemented yet!");
    return null;
  }
}
