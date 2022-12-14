// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.*;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class Parser {
  public static Root parse(Resolution resolution, List<Lexeme> lex) {
    var parser = new Parser(resolution, new ArrayList<>(), lex, 0, null);
    return parser.parse();
  }

  private final Resolution      resolution;
  private final List<Statement> statements;
  private final List<Lexeme>    lex;

  private int        index;
  private Portion    unexpected;
  private Expression expression;

  private Parser(Resolution resolution, List<Statement> statements,
    List<Lexeme> lex, int index, Portion unexpected) {
    this.resolution = resolution;
    this.statements = statements;
    this.lex        = lex;
    this.index      = index;
    this.unexpected = unexpected;
  }

  private Root parse() {
    while (has()) {
      if (parseStatement()) {
        unexpected();
        continue;
      }
      if (unexpected == null) {
        unexpected = get().portion;
      } else {
        unexpected = Portion.of(unexpected, get().portion);
      }
      index++;
    }
    unexpected();
    if (!(get() instanceof EOF eof)) {
      resolution.error("PARSER", "There is no EOF token!");
      return null;
    }
    return Root.of(statements, eof);
  }

  private boolean has() {
    return index < lex.size() - 1;
  }

  private Lexeme get() {
    return lex.get(index);
  }

  private Lexeme consume() {
    var lexeme = get();
    index++;
    return lexeme;
  }

  private void skip(Class<? extends Lexeme> type) {
    var start = ++index;
    while (has() && !type.isInstance(get())) {
      index++;
    }
    resolution.info("PARSER", portion(start),
      "This portion is skipped because of the previous error.");
    index++;
  }

  private Portion portion(int start) {
    return Portion.of(lex.get(start).portion, get().portion);
  }

  private boolean parseStatement() {
    return parseDefinition();
  }

  private boolean hasExpression() {
    return expression != null;
  }

  private Expression getExpression() {
    var result = expression;
    expression = null;
    return result;
  }

  private boolean parseDefinition() {
    if (!(get() instanceof Identifier name)) {
      return false;
    }
    index++;

    if (!has()) {
      resolution.error("PARSER", name.portion,
        "There is no `:` after the name in the definition of `%s`!"
          .formatted(name));
      return true;
    }
    if (!(get() instanceof Colon separator)) {
      resolution.error("PARSER", get().portion,
        "Expected a `:` after the name in the definition of `%s`!"
          .formatted(name));
      skip(Semicolon.class);
      return true;
    }
    index++;

    if (!has()) {
      resolution.error("PARSER", separator.portion,
        "There is no type after the `:` in the definition of `%s`!"
          .formatted(name));
      return true;
    }
    var start = index;
    if (!parseExpression(0)) {
      resolution.error("PARSER", portion(start),
        "Expected a type after the `:` in the definition of `%s`!"
          .formatted(name));
      skip(Semicolon.class);
      return true;
    }
    if (!hasExpression()) {
      skip(Semicolon.class);
      return true;
    }
    var type = getExpression();

    if (!has()) {
      resolution.error("PARSER", type.portion,
        "There is no `=` after the type in the definition of `%s`!"
          .formatted(name));
      return true;
    }
    if (!(get() instanceof Equal assignment)) {
      resolution.error("PARSER", get().portion,
        "Expected a `=` after the type in the definition of `%s`!"
          .formatted(name));
      skip(Semicolon.class);
      return true;
    }
    index++;

    if (!has()) {
      resolution.error("PARSER", assignment.portion,
        "There is no value after the `=` in the definition of `%s`!"
          .formatted(name));
      return true;
    }
    start = index;
    if (!parseExpression(0)) {
      resolution.error("PARSER", portion(start),
        "Expected a value after the `=` in the definition of `%s`!"
          .formatted(name));
      skip(Semicolon.class);
      return true;
    }
    if (!hasExpression()) {
      skip(Semicolon.class);
      return true;
    }
    var value = getExpression();

    if (!has()) {
      resolution.error("PARSER", value.portion,
        "There is no `;` after the value in the definition of `%s`!"
          .formatted(name));
      return true;
    }
    if (!(get() instanceof Semicolon end)) {
      resolution.error("PARSER", get().portion,
        "Expected a `;` after the value in the definition of `%s`!"
          .formatted(name));
      skip(Semicolon.class);
      return true;
    }
    index++;

    statements.add(Definition.of(name, type, value));
    return true;
  }

  private void unexpected() {
    if (unexpected != null) {
      resolution.error("PARSER", unexpected,
        "Expected a statement instead of %s!"
          .formatted(unexpected.length() > 1 ? "these tokens" : "this token"));
      unexpected = null;
    }
  }

  private boolean parseExpression(int precedence) {
    var parsed = false;
    overall: for (var i = precedence; i < Operator.precedences(); i++) {
      for (var j = 0; j < Operator.length(i); j++) {
        if (parseOperator(i, j)) {
          parsed = true;
          if (!hasExpression()) {
            break overall;
          }
          i = precedence - 1;
          continue overall;
        }
      }
    }
    return parsed;
  }

  private boolean parseOperator(int precedence, int inlevel) {
    return switch (Operator.get(precedence, inlevel)) {
      case NullaryOperator nullary -> parseNullary(nullary);
      case PrenaryOperator prenary -> parsePrenary(prenary, precedence);
      case PostaryOperator postary -> parsePostary(postary);
      case CirnaryOperator cirnary -> parseCirnary(cirnary);
      case BinaryOperator binary -> parseBinary(binary, precedence);
      case VariaryOperator variary -> parseVariary(variary);
    };
  }

  private boolean parseNullary(NullaryOperator nullary) {
    if (hasExpression() || !nullary.operator(get())) {
      return false;
    }
    expression = NullaryExpression.of(nullary, consume());
    return true;
  }

  private boolean parsePrenary(PrenaryOperator prenary, int precedence) {
    if (hasExpression() || !prenary.operator(get())) {
      return false;
    }
    var operator = consume();

    if (!has()) {
      resolution.error("PARSER", operator.portion,
        "There is no operand after the `%s` in the prenary operation!"
          .formatted(operator));
      return true;
    }
    var start = index;
    if (!parseExpression(precedence)) {
      resolution.error("PARSER", portion(start),
        "Expected an operand after the `%s` in the prenary operation!"
          .formatted(operator));
      return true;
    }
    if (!hasExpression()) {
      return true;
    }

    expression = PrenaryExpression.of(prenary, operator, getExpression());
    return true;
  }

  private boolean parsePostary(PostaryOperator postary) {
    if (!hasExpression() || !has() || !postary.operator(get())) {
      return false;
    }

    expression = PostaryExpression.of(postary, getExpression(), consume());
    return true;
  }

  private boolean parseCirnary(CirnaryOperator cirnary) {
    if (hasExpression() || !cirnary.left(get())) {
      return false;
    }
    var left = consume();

    if (!has()) {
      resolution.error("PARSER", left.portion,
        "There is no operand after the `%s` in the cirnary operation!"
          .formatted(left));
      return true;
    }
    var start = index;
    if (!parseExpression(0)) {
      resolution.error("PARSER", portion(start),
        "Expected an operand after the `%s` in the cirnary operation!"
          .formatted(left));
      return true;
    }
    if (!hasExpression()) {
      return true;
    }

    if (!has()) {
      resolution.error("PARSER",
        Portion.of(left.portion, getExpression().portion),
        "There is no matching `%s` for the `%s` in the cirnary operation!"
          .formatted(cirnary.right(), left));
      resolution.info("PARSER", left.portion,
        "Cirnary operator is opened here.");
      return true;
    }
    if (!cirnary.right(get())) {
      resolution.error("PARSER", get().portion,
        "Expected a matching `%s` for the `%s` in the cirnary operation!"
          .formatted(cirnary.right(), left));
      resolution.info("PARSER", left.portion,
        "Cirnary operator is opened here.");
      return true;
    }

    expression = CirnaryExpression.of(cirnary, left, getExpression(),
      consume());
    return true;
  }

  private boolean parseBinary(BinaryOperator binary, int precedence) {
    if (!hasExpression() || !has() || !binary.operator(get())) {
      return false;
    }

    var left     = getExpression();
    var operator = consume();

    if (!has()) {
      resolution.error("PARSER", operator.portion,
        "There is no operand after the `%s` in the binary operation!"
          .formatted(operator));
      return true;
    }
    var start = index;
    if (!parseExpression(precedence + 1)) {
      resolution.error("PARSER", portion(start),
        "Expected an operand after the `%s` in the binary operation!"
          .formatted(operator));
      return true;
    }
    if (!hasExpression()) {
      return true;
    }

    expression = BinaryExpression.of(binary, left, operator, getExpression());
    return true;
  }

  private boolean parseVariary(VariaryOperator variary) {
    if (!hasExpression() || !has() || !variary.left(get())) {
      return false;
    }

    var first     = getExpression();
    var left      = consume();
    var remaining = new ArrayList<Expression>();
    var between   = new ArrayList<Lexeme>();

    if (!has()) {
      resolution.error("PARSER", Portion.of(first.portion, left.portion),
        "There is no matching `%s` for the `%s` in the variary operation!"
          .formatted(variary.right(), left));
      resolution.info("PARSER", left.portion,
        "Variary operator is opened here.");
      return true;
    }

    if (!parseExpression(0)) {
      if (!variary.right(get())) {
        resolution.error("PARSER", get().portion,
          "Expected a matching `%s` for the `%s` in the variary operation!"
            .formatted(variary.right(), left));
        resolution.info("PARSER", left.portion,
          "Variary operator is opened here.");
        return true;
      }

      expression = VariaryExpression.of(variary, first, left, remaining,
        between,
        consume());
      return true;
    }
    if (!hasExpression()) {
      return true;
    }

    remaining.add(getExpression());

    while (true) {
      if (!has()) {
        resolution.error("PARSER",
          Portion.of(first.portion,
            remaining.get(remaining.size() - 1).portion),
          "There is no matching `%s` for the `%s` or a `%s` with another operand, in the variary operation!"
            .formatted(variary.right(), variary.between(), left));
        resolution.info("PARSER", left.portion,
          "Variary operator is opened here.");
        return true;
      }
      if (variary.right(get())) {
        break;
      }
      if (!variary.between(get())) {
        resolution.error("PARSER",
          Portion.of(first.portion,
            remaining.get(remaining.size() - 1).portion),
          "Expected a matching `%s` for the `%s` or a `%s` with another operand, in the variary operation!"
            .formatted(variary.right(), variary.between(), left));
        resolution.info("PARSER", left.portion,
          "Variary operator is opened here.");
        return true;
      }

      between.add(consume());

      if (!has()) {
        resolution.error("PARSER", between.get(between.size() - 1).portion,
          "There is no operand after the `%s` in the variary operation!"
            .formatted(variary.between(), left));
        return true;
      }
      if (!parseExpression(0)) {
        resolution.error("PARSER", between.get(between.size() - 1).portion,
          "Expected an operand after the `%s` in the variary operation!"
            .formatted(variary.between(), left));
        return true;
      }
      if (!hasExpression()) {
        return true;
      }

      remaining.add(getExpression());
    }

    expression = VariaryExpression.of(variary, first, left, remaining, between,
      consume());
    return true;
  }
}
