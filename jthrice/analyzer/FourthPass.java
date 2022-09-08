// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.math.*;
import java.util.function.*;

import jthrice.launcher.*;
import jthrice.lexer.*;
import jthrice.parser.*;

/** Resolves the definition of global variables and functions. */
final class FourthPass {
  /** Resolve the given program node to the given solution and report to the
   * given resolution. */
  static void resolve(Resolution resolution, HalfSolution solution,
    Node.Program program) {
    for (var statement : program.statements) {
      FourthPass.resolveStatement(resolution, solution, statement);
    }
  }

  /** Resolve the given statement node to the given solution and report to the
   * given resolution. */
  private static void resolveStatement(Resolution resolution,
    HalfSolution solution, Node.Statement statement) {
    switch (statement) {
      case Node.Definition definition ->
        FourthPass.resolveDefinition(resolution, solution, definition);
    }
  }

  /** Resolve the given definition node to the given solution and report to the
   * given resolution. */
  private static void resolveDefinition(Resolution resolution,
    HalfSolution solution, Node.Definition definition) {
    var resolved = solution.resolved.get(definition.name.value);
    if (resolved != null) {
      resolution.error("ANALYZER", definition.portion,
        "Definition is already resolved!");
      return;
    }
    var unresolved = solution.unresolved.get(definition.name.value);
    if (unresolved == null) {
      resolution.error("ANALYZER", definition.portion,
        "Definition is not unresolved!");
      return;
    }
    var type = FourthPass.resolveExpression(resolution, solution, Type.META,
      definition.type);

    if (!type.known()) {
      resolution.error("ANALYZER", definition.type.portion,
        "Type must be known at compile-time!");
      return;
    }
    if (type.value instanceof Scalar.Rinf) {
      resolution.error("ANALYZER", definition.type.portion,
        "Cannot have a variable of infinite precision real, `%s`!"
          .formatted(type.value));
      return;
    }

    var bound = FourthPass.resolveExpression(resolution, solution,
      (Type) type.value, definition.value);

    solution.unresolved.remove(definition.name.value);
    solution.resolved.put(definition.name.value,
      Variable.of(definition.name, bound, definition.value));
  }

  /** Resolve the type of the given expression node to the given solution and
   * report to the given resolution. */
  private static Evaluation resolveExpression(Resolution resolution,
    HalfSolution solution, Type expected, Node.Expression expression) {
    return switch (expression) {
      case Node.Nofix nofix ->
        FourthPass.resolveNofix(resolution, solution, expected, nofix);
      case Node.Prefix prefix ->
        FourthPass.resolvePrefix(resolution, solution, expected, prefix);
      case Node.Postfix postfix ->
        FourthPass.resolvePostfix(resolution, solution, expected, postfix);
      case Node.Infix infix ->
        FourthPass.resolveInfix(resolution, solution, expected, infix);
      case Node.Outfix outfix ->
        FourthPass.resolveOutfix(resolution, solution, expected, outfix);
      case Node.Knitfix knitfix ->
        FourthPass.resolveKnitfix(resolution, solution, expected, knitfix);
    };
  }

  /** Resolve the type of the given nofix node to the given solution and report
   * to the given resolution. */
  private static Evaluation resolveNofix(Resolution resolution,
    HalfSolution solution, Type expected, Node.Nofix nofix) {
    if (nofix.operator == Operator.LITERAL) {
      return switch (nofix.first) {
        case Lexeme.Number number -> {
          if (!(expected instanceof Scalar scalar)) {
            resolution.error("ANALYZER", nofix.portion,
              "Expected `%s` instead of number `%s`!".formatted(expected,
                number.value));
            yield null;
          }
          if (!scalar.holds(number.value)) {
            resolution.error("ANALYZER", nofix.portion,
              "Number `%s` cannot be stored in a `%s`!".formatted(number.value,
                scalar));
            yield null;
          }
          yield Evaluation.ofNofix(scalar, number.value, nofix.operator,
            number);
        }
        default -> {
          resolution.error("ANALYZER", nofix.portion,
            "Unknown literal operator lexeme!");
          yield null;
        }
      };
    }
    if (nofix.operator == Operator.ACCESS) {
      return FourthPass.resolveAccess(resolution, solution, expected,
        nofix.first);
    }
    resolution.error("ANALYZER", nofix.portion, "Unknown nofix operator!");
    return null;
  }

  /** Resolve the access to the given name in the given solution and report to
   * the given resolution. */
  private static Evaluation resolveAccess(Resolution resolution,
    HalfSolution solution, Type expected, Lexeme name) {
    var accessed = solution.resolved.get(name.toString());
    if (accessed == null) {
      resolution.error("ANALYZER", name.portion,
        "Could not find the accessed variable `%s`!"
          .formatted(name.toString()));
      var later = solution.unresolved.get(name.toString());
      if (later != null) {
        resolution.info("ANALYZER", later.definition.portion,
          "The variable is defined later here.");
      }
      return null;
    }
    var result = Evaluation.ofNofix(accessed.evaluation.type,
      accessed.evaluation.value, Operator.ACCESS, name);
    if (result != null && result.type != expected) {
      resolution.error("ANALYZER", name.portion,
        "Expected `%s` instead of `%s`!".formatted(expected, result.type));
      return null;
    }
    return result;
  }

  /** Resolve the type of the given prefix node to the given solution and report
   * to the given resolution. */
  private static Evaluation resolvePrefix(Resolution resolution,
    HalfSolution solution, Type expected, Node.Prefix prefix) {
    var operand = FourthPass.resolveExpression(resolution, solution, expected,
      prefix.last);
    if (operand == null) {
      resolution.error("ANALYZER", prefix.last.portion,
        "Could not resolve the type of the operand!");
      return null;
    }
    if (prefix.operator == Operator.POSATE) {
      if (!(operand.type instanceof Scalar)) {
        resolution.error("ANALYZER", prefix.portion,
          "Operator `%s` can not be used with `%s`!".formatted(prefix.before,
            operand.type));
        return null;
      }
      return operand;
    }
    if (prefix.operator == Operator.NEGATE) {
      if (!(operand.type instanceof Scalar)) {
        resolution.error("ANALYZER", prefix.portion,
          "Operator `%s` can not be used with `%s`!".formatted(prefix.before,
            operand.type));
        return null;
      }
      Object value = null;
      if (operand.known()) {
        value = ((BigDecimal) operand.value).negate();
      }
      return Evaluation.ofPrefix(operand.type, value, prefix.operator,
        prefix.before, operand);
    }
    resolution.error("ANALYZER", prefix.portion, "Unknown prefix operator!");
    return null;
  }

  /** Resolve the type of the given postfix node to the given solution and
   * report to the given resolution. */
  private static Evaluation resolvePostfix(Resolution resolution,
    HalfSolution solution, Type expected, Node.Postfix postfix) {
    var operand = FourthPass.resolveExpression(resolution, solution, expected,
      postfix.first);
    if (operand == null) {
      resolution.error("ANALYZER", postfix.first.portion,
        "Could not resolve the type of the operand!");
      return null;
    }
    resolution.error("ANALYZER", postfix.portion, "Unknown postfix operator!");
    return null;
  }

  /** Resolve the type of the given infix node to the given solution and report
   * to the given resolution. */
  private static Evaluation resolveInfix(Resolution resolution,
    HalfSolution solution, Type expected, Node.Infix infix) {
    var left  = FourthPass.resolveExpression(resolution, solution, expected,
      infix.first);
    var right = FourthPass.resolveExpression(resolution, solution, expected,
      infix.last);
    if (left == null) {
      resolution.error("ANALYZER", infix.first.portion,
        "Could not resolve the type of the left operand!");
      return null;
    }
    if (right == null) {
      resolution.error("ANALYZER", infix.last.portion,
        "Could not resolve the type of the right operand!");
      return null;
    }
    if (!(expected instanceof Scalar)) {
      resolution.error("ANALYZER", infix.portion,
        "Operator `%s` can not be used with `%s`!".formatted(infix.between,
          expected));
      return null;
    }
    BiFunction<BigDecimal, BigDecimal, BigDecimal> operation = null;
    if (infix.operator == Operator.ADD) {
      operation = BigDecimal::add;
    }
    if (infix.operator == Operator.SUBTRACT) {
      operation = BigDecimal::subtract;
    }
    if (infix.operator == Operator.MULTIPLY) {
      operation = BigDecimal::multiply;
    }
    if (infix.operator == Operator.DIVIDE) {
      operation = (divleft, divright) -> divleft.divide(divright,
        RoundingMode.HALF_UP);
    }
    if (infix.operator == Operator.REMAINDER) {
      operation = BigDecimal::remainder;
    }
    if (operation != null) {
      Object value = null;
      if (left.known() && right.known()) {
        value = operation.apply((BigDecimal) left.value,
          (BigDecimal) right.value);
      }
      return Evaluation.ofInfix(expected, value, infix.operator, infix.between,
        left, right);
    }
    resolution.error("ANALYZER", infix.portion, "Unknown postfix operator!");
    return null;
  }

  /** Resolve the type of the given outfix node to the given solution and report
   * to the given resolution. */
  private static Evaluation resolveOutfix(Resolution resolution,
    HalfSolution solution, Type expected, Node.Outfix outfix) {
    var operand = FourthPass.resolveExpression(resolution, solution, expected,
      outfix.middle);
    if (operand == null) {
      resolution.error("ANALYZER", outfix.middle.portion,
        "Could not resolve the type of the operand!");
      return null;
    }
    if (outfix.operator == Operator.GROUP) {
      return Evaluation.ofOutfix(operand.type, operand.value, outfix.operator,
        outfix.before, outfix.after, operand);
    }
    resolution.error("ANALYZER", outfix.portion, "Unknown outfix operator!");
    return null;
  }

  /** Resolve the type of the given knitfix node to the given solution and
   * report to the given resolution. */
  private static Evaluation resolveKnitfix(Resolution resolution,
    HalfSolution solution, Type expected, Node.Knitfix knitfix) {
    resolution.error("ANALYZER", knitfix.portion, "Unknown knitfix operator!");
    return null;
  }

  /** Constructor. */
  private FourthPass() {
  }
}
