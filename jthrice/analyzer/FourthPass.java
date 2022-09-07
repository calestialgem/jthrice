// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.math.*;

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
      resolveStatement(resolution, solution, statement);
    }
  }

  /** Resolve the given statement node to the given solution and report to the
   * given resolution. */
  private static void resolveStatement(Resolution resolution,
    HalfSolution solution, Node.Statement statement) {
    switch (statement) {
      case Node.Definition definition ->
        resolveDefinition(resolution, solution, definition);
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
    var type = resolveExpression(resolution, solution, definition.type);

    if (type.type != Type.META) {
      resolution.error("ANALYZER", definition.type.portion,
        "Expected a type instead of `%s`!".formatted(type));
      return;
    }
    if (!type.known()) {
      resolution.error("ANALYZER", definition.type.portion,
        "Type must be known at compile-time!");
      return;
    }

    var boundType = resolveExpression(resolution, solution, definition.value);

    if (boundType.type != type.value
      && (boundType.type != Scalar.RINF || type.value instanceof Scalar)) {
      resolution.error("ANALYZER", definition.value.portion,
        "Variable of `%s` cannot be bound to a `%s`!".formatted(type.value,
          boundType.type));
      return;
    }
    if (type.value instanceof Scalar.Rinf) {
      resolution.error("ANALYZER", definition.type.portion,
        "Cannot have a variable of infinite precision real, `%s`!"
          .formatted(type.value));
      return;
    }

    solution.unresolved.remove(definition.name.value);
    solution.resolved.put(definition.name.value, Variable.of(definition.name,
      Evaluation.of((Type) type.value, boundType.value), definition.value));
  }

  /** Resolve the type of the given expression node to the given solution and
   * report to the given resolution. */
  private static Evaluation resolveExpression(Resolution resolution,
    HalfSolution solution, Node.Expression expression) {
    return switch (expression) {
      case Node.Nofix nofix -> resolveNofix(resolution, solution, nofix);
      case Node.Prefix prefix -> resolvePrefix(resolution, solution, prefix);
      case Node.Postfix postfix ->
        resolvePostfix(resolution, solution, postfix);
      case Node.Infix infix -> resolveInfix(resolution, solution, infix);
      case Node.Outfix outfix -> resolveOutfix(resolution, solution, outfix);
      case Node.Knitfix knitfix ->
        resolveKnitfix(resolution, solution, knitfix);
    };
  }

  /** Resolve the type of the given nofix node to the given solution and report
   * to the given resolution. */
  private static Evaluation resolveNofix(Resolution resolution,
    HalfSolution solution, Node.Nofix nofix) {
    if (nofix.operator == Operator.LITERAL) {
      return switch (nofix.first) {
        case Lexeme.Number number -> Evaluation.of(Scalar.RINF, number.value);
        default -> {
          resolution.error("ANALYZER", nofix.portion,
            "Unknown literal operator lexeme!");
          yield null;
        }
      };
    }
    if (nofix.operator == Operator.ACCESS) {
      return switch (nofix.first) {
        case Lexeme.I1 i1 -> TypeSymbol.I1.evaluation;
        case Lexeme.I2 i2 -> TypeSymbol.I2.evaluation;
        case Lexeme.I4 i4 -> TypeSymbol.I4.evaluation;
        case Lexeme.I8 i8 -> TypeSymbol.I8.evaluation;
        case Lexeme.Ix ix -> TypeSymbol.IX.evaluation;
        case Lexeme.U1 u1 -> TypeSymbol.U1.evaluation;
        case Lexeme.U2 u2 -> TypeSymbol.U2.evaluation;
        case Lexeme.U4 u4 -> TypeSymbol.U4.evaluation;
        case Lexeme.U8 u8 -> TypeSymbol.U8.evaluation;
        case Lexeme.Ux ux -> TypeSymbol.UX.evaluation;
        case Lexeme.F4 f4 -> TypeSymbol.F4.evaluation;
        case Lexeme.F8 f8 -> TypeSymbol.F8.evaluation;
        case Lexeme.Rinf rinf -> TypeSymbol.RINF.evaluation;
        case Lexeme.Type type -> TypeSymbol.META.evaluation;
        case Lexeme.Identifier identifier -> {
          var accessed = solution.resolved.get(identifier.value);
          if (accessed == null) {
            resolution.error("ANALYZER", identifier.portion,
              "Could not find the accessed variable `%s`!"
                .formatted(identifier.value));
            var later = solution.unresolved.get(identifier.value);
            if (later != null) {
              resolution.info("ANALYZER", later.definition.portion,
                "The variable is defined later here.");
            }
            yield null;
          }
          yield accessed.evaluation;
        }
        default -> {
          resolution.error("ANALYZER", nofix.portion,
            "Unknown access operator lexeme!");
          yield null;
        }
      };
    }
    resolution.error("ANALYZER", nofix.portion, "Unknown nofix operator!");
    return null;
  }

  /** Resolve the type of the given prefix node to the given solution and report
   * to the given resolution. */
  private static Evaluation resolvePrefix(Resolution resolution,
    HalfSolution solution, Node.Prefix prefix) {
    var operand = resolveExpression(resolution, solution, prefix.last);
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
      if (operand.known()) {
        return Evaluation.of(operand.type,
          ((BigDecimal) operand.value).negate());
      }
      return Evaluation.of(operand.type);
    }
    resolution.error("ANALYZER", prefix.portion, "Unknown prefix operator!");
    return null;
  }

  /** Resolve the type of the given postfix node to the given solution and
   * report to the given resolution. */
  private static Evaluation resolvePostfix(Resolution resolution,
    HalfSolution solution, Node.Postfix postfix) {
    var operand = resolveExpression(resolution, solution, postfix.first);
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
    HalfSolution solution, Node.Infix infix) {
    var left  = resolveExpression(resolution, solution, infix.first);
    var right = resolveExpression(resolution, solution, infix.last);
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
    if (left != right) {
      resolution.error("ANALYZER", infix.portion,
        "Operator `%s` cannot be used with `%s` on left and `%s` on right!"
          .formatted(infix.between, left.type, right.type));
      return null;
    }
    if (infix.operator == Operator.ADD) {
      if (!(left.type instanceof Scalar)) {
        resolution.error("ANALYZER", infix.portion,
          "Operator `%s` can not be used with `%s`!".formatted(infix.between,
            left));
        return null;
      }
      if (left.known() && right.known()) {
        return Evaluation.of(left.type,
          ((BigDecimal) left.value).add((BigDecimal) right.value));
      }
      return Evaluation.of(left.type);
    }
    if (infix.operator == Operator.SUBTRACT) {
      if (!(left.type instanceof Scalar)) {
        resolution.error("ANALYZER", infix.portion,
          "Operator `%s` can not be used with `%s`!".formatted(infix.between,
            left));
        return null;
      }
      if (left.known() && right.known()) {
        return Evaluation.of(left.type,
          ((BigDecimal) left.value).subtract((BigDecimal) right.value));
      }
      return Evaluation.of(left.type);
    }
    if (infix.operator == Operator.MULTIPLY) {
      if (!(left.type instanceof Scalar)) {
        resolution.error("ANALYZER", infix.portion,
          "Operator `%s` can not be used with `%s`!".formatted(infix.between,
            left));
        return null;
      }
      if (left.known() && right.known()) {
        return Evaluation.of(left.type,
          ((BigDecimal) left.value).multiply((BigDecimal) right.value));
      }
      return Evaluation.of(left.type);
    }
    if (infix.operator == Operator.DIVIDE) {
      if (!(left.type instanceof Scalar)) {
        resolution.error("ANALYZER", infix.portion,
          "Operator `%s` can not be used with `%s`!".formatted(infix.between,
            left));
        return null;
      }
      if (left.known() && right.known()) {
        return Evaluation.of(left.type,
          ((BigDecimal) left.value).divide((BigDecimal) right.value));
      }
      return Evaluation.of(left.type);
    }
    if (infix.operator == Operator.REMAINDER) {
      if (!(left.type instanceof Scalar)) {
        resolution.error("ANALYZER", infix.portion,
          "Operator `%s` can not be used with `%s`!".formatted(infix.between,
            left));
        return null;
      }
      if (left.known() && right.known()) {
        return Evaluation.of(left.type,
          ((BigDecimal) left.value).remainder((BigDecimal) right.value));
      }
      return Evaluation.of(left.type);
    }
    resolution.error("ANALYZER", infix.portion, "Unknown postfix operator!");
    return null;
  }

  /** Resolve the type of the given outfix node to the given solution and report
   * to the given resolution. */
  private static Evaluation resolveOutfix(Resolution resolution,
    HalfSolution solution, Node.Outfix outfix) {
    var operand = resolveExpression(resolution, solution, outfix.middle);
    if (operand == null) {
      resolution.error("ANALYZER", outfix.middle.portion,
        "Could not resolve the type of the operand!");
      return null;
    }
    if (outfix.operator == Operator.GROUP) {
      return operand;
    }
    resolution.error("ANALYZER", outfix.portion, "Unknown outfix operator!");
    return null;
  }

  /** Resolve the type of the given knitfix node to the given solution and
   * report to the given resolution. */
  private static Evaluation resolveKnitfix(Resolution resolution,
    HalfSolution solution, Node.Knitfix knitfix) {
    resolution.error("ANALYZER", knitfix.portion, "Unknown knitfix operator!");
    return null;
  }

  /** Constructor. */
  private FourthPass() {
  }
}
