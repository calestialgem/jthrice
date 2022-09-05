// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Name: GPL-3.0-or-later

package jthrice.resolver;

import java.util.*;

import jthrice.launcher.*;
import jthrice.lexer.*;
import jthrice.parser.*;

/** Resolves a program node to a solution. */
public final class Resolver {
  /** Resolves the given program node and report to the given resolution. */
  public static Solution resolve(Resolution resolution, Node.Program program) {
    var solution = Resolver.builtin();
    var failed   = false;
    for (var statement : program.statements) {
      if (!Resolver.resolveStatement(resolution, solution, statement)) {
        failed = true;
      }
    }
    if (failed) {
      return null;
    }
    return solution;
  }

  /** Solution with built-in types and operators. */
  private static Solution builtin() {
    var builtin = Solution.of();
    Resolver.builtinArithmetic(builtin);
    return builtin;
  }

  /** Adds the built-in arithmetic types and their operators to the given
   * solution. */
  private static void builtinArithmetic(Solution builtin) {
    var types = new HashMap<String, Type>();
    types.put("i1", Type.ofI1());
    types.put("i2", Type.ofI2());
    types.put("i4", Type.ofI4());
    types.put("i8", Type.ofI8());
    types.put("ix", Type.ofIx());
    types.put("u1", Type.ofU1());
    types.put("u2", Type.ofU2());
    types.put("u4", Type.ofU4());
    types.put("u8", Type.ofU8());
    types.put("ux", Type.ofUx());
    types.put("f4", Type.ofF4());
    types.put("f8", Type.ofF8());
    types.put("rinf", Type.ofRinf());
    types.put("type", Type.ofMeta());
    builtin.types.putAll(types);
  }

  /** Try to resolve the given statement node to the given solution and report
   * to the given resolution. */
  private static boolean resolveStatement(Resolution resolution,
    Solution solution, Node.Statement statement) {
    return switch (statement) {
      case Node.Definition defition ->
        Resolver.resolveDefinition(resolution, solution, defition);
    };
  }

  /** Try to resolve the given definition node to the given solution and report
   * to the given resolution. */
  private static boolean resolveDefinition(Resolution resolution,
    Solution solution, Node.Definition definition) {
    var same = solution.variables.keySet().stream()
      .filter(definition.name::equals).findAny();
    if (same.isPresent()) {
      resolution.error("RESOLVER", definition.name.portion,
        "Another variable with the same name is already defined!");
      resolution.info("RESOLVER", same.get().portion,
        "Previous definition was here.");
      return false;
    }

    var type = Resolver.resolveExpression(resolution, solution,
      definition.type);
    if (type == null) {
      resolution.error("RESOLVER", definition.type.portion,
        "Could not resolve the type!");
      return false;
    }

    solution.variables.put(definition.name, type);
    return true;
  }

  /** Try to resolve the given expression node to the given solution and report
   * to the given resolution. */
  private static Type resolveExpression(Resolution resolution,
    Solution solution, Node.Expression expression) {
    switch (expression) {
      case Node.Literal literal:
        return Resolver.resolveLiteral(resolution, solution, literal);
      case Node.Access access:
        return Resolver.resolveAccess(resolution, solution, access);
      case Node.Group group:
        return Resolver.resolveGroup(resolution, solution, group);
      default:
        resolution.error("RESOLVER", expression.portion, "Expected a type!");
        return null;
    }
  }

  /** Try to resolve the given literal node to the given solution and report to
   * the given resolution. */
  private static Type resolveLiteral(Resolution resolution, Solution solution,
    Node.Literal literal) {
    switch (literal.value) {
      case Lexeme.I1 i1:
        return solution.types.get(i1.toString());
      case Lexeme.I2 i2:
        return solution.types.get(i2.toString());
      case Lexeme.I4 i4:
        return solution.types.get(i4.toString());
      case Lexeme.I8 i8:
        return solution.types.get(i8.toString());
      case Lexeme.Ix ix:
        return solution.types.get(ix.toString());
      case Lexeme.U1 u1:
        return solution.types.get(u1.toString());
      case Lexeme.U2 u2:
        return solution.types.get(u2.toString());
      case Lexeme.U4 u4:
        return solution.types.get(u4.toString());
      case Lexeme.U8 u8:
        return solution.types.get(u8.toString());
      case Lexeme.Ux ux:
        return solution.types.get(ux.toString());
      case Lexeme.F4 f4:
        return solution.types.get(f4.toString());
      case Lexeme.F8 f8:
        return solution.types.get(f8.toString());
      case Lexeme.Rinf rinf:
        resolution.error("RESOLVER", literal.portion,
          "Infinite precision real type is not supported!");
        return null;
      case Lexeme.Type type:
        resolution.error("RESOLVER", literal.portion,
          "Meta type is not supported!");
        return null;
      default:
        resolution.error("RESOLVER", literal.portion, "Expected a type!");
        return null;
    }
  }

  /** Try to resolve the given access node to the given solution and report to
   * the given resolution. */
  private static Type resolveAccess(Resolution resolution, Solution solution,
    Node.Access access) {
    var result = solution.types.get(access.name.value);
    if (result == null) {
      resolution.error("RESOLVER", access.portion,
        "Could not find a type with the given name!");
    }
    return result;
  }

  /** Try to resolve the given group node to the given solution and report to
   * the given resolution. */
  private static Type resolveGroup(Resolution resolution, Solution solution,
    Node.Group group) {
    return Resolver.resolveExpression(resolution, solution, group.elevated);
  }
}
