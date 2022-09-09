// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.launcher.*;
import jthrice.parser.*;

final class FirstPass {
  static void resolve(Resolution resolution, HalfSolution solution,
    Node.Program program) {
    for (var statement : program.statements) {
      var unresolved = FirstPass.resolveStatement(resolution, solution,
        statement);
      if (unresolved != null) {
        solution.unresolved.put(unresolved.name, unresolved);
      }
    }
  }

  private static Unresolved resolveStatement(Resolution resolution,
    HalfSolution solution, Node.Statement statement) {
    return switch (statement) {
      case Node.Definition definition ->
        FirstPass.resolveDefinition(resolution, solution, definition);
    };
  }

  private static Unresolved resolveDefinition(Resolution resolution,
    HalfSolution solution, Node.Definition definition) {
    var builtIn = solution.resolved.get(definition.name.value);
    if (builtIn != null) {
      resolution.error("ANALYZER", definition.name.portion,
        "Name clashes with the built-in symbol `%s`!".formatted(builtIn.name));
      return null;
    }
    var userDefined = solution.unresolved.get(definition.name.value);
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

  private FirstPass() {
  }
}
