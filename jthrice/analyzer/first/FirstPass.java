// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer.first;

import jthrice.analyzer.*;
import jthrice.launcher.*;
import jthrice.parser.*;

/** Defines the built-in symbols, and records the custom-defined ones without
 * resolving them. */
public final class FirstPass {
  /** Resolve the given program node and report to the given resolution. */
  public static FirstSolution resolve(Resolution resolution,
    Node.Program program) {
    var solution = FirstSolution.of();
    for (var symbol : Symbol.BUILT_IN) {
      solution.symbols.put(symbol.name, symbol);
    }
    for (var statement : program.statements) {
      var unresolved = resolveStatement(resolution, solution, statement);
      if (unresolved != null) {
        solution.unresolveds.put(unresolved.name, unresolved);
      }
    }
    return solution;
  }

  /** Resolve the given statement node and report to the given resolution. */
  private static Unresolved resolveStatement(Resolution resolution,
    FirstSolution solution, Node.Statement statement) {
    return switch (statement) {
      case Node.Definition definition ->
        resolveDefinition(resolution, solution, definition);
    };
  }

  /** Resolve the given definition node and report to the given resolution. */
  private static Unresolved resolveDefinition(Resolution resolution,
    FirstSolution solution, Node.Definition definition) {
    var builtIn = solution.symbols.get(definition.name.value);
    if (builtIn != null) {
      resolution.error("ANALYZER", definition.name.portion,
        "Name clashes with the built-in symbol `%s`!".formatted(builtIn.name));
      return null;
    }
    var userDefined = solution.unresolveds.get(definition.name.value);
    if (userDefined != null) {
      resolution.error("ANALYZER", definition.name.portion,
        "Name clashes with the previously defined symbol `%s`!"
          .formatted(userDefined.name));
      resolution.info("ANALYZER", userDefined.definition.portion,
        "Previous decleration was here.");
      return null;
    }
    return Unresolved.of(definition.name, definition.type, definition.value);
  }
}
