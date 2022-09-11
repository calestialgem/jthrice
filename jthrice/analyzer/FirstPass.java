// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.launcher.*;
import jthrice.parser.*;

final class FirstPass {
  static void resolve(Resolution resolution, HalfSolution solution,
    Root root) {
    var pass = new FirstPass(resolution, solution, root);
    pass.resolve();
  }

  private final Resolution   resolution;
  private final HalfSolution solution;
  private final Root         root;

  private FirstPass(Resolution resolution, HalfSolution solution, Root root) {
    this.resolution = resolution;
    this.solution   = solution;
    this.root       = root;
  }

  private void resolve() {
    for (var statement : root.statements) {
      resolveStatement(statement);
    }
  }

  private void resolveStatement(Statement statement) {
    switch (statement) {
      case Definition definition -> resolveDefinition(definition);
    }
  }

  private void resolveDefinition(Definition definition) {
    var builtIn = solution.resolved.get(definition.name.toString());
    if (builtIn != null) {
      resolution.error("ANALYZER", definition.name.portion,
        "Name clashes with the built-in symbol `%s`!".formatted(builtIn.name));
      return;
    }
    var userDefined = solution.unresolved.get(definition.name.toString());
    if (userDefined != null) {
      resolution.error("ANALYZER", definition.name.portion,
        "Name clashes with the previously defined symbol `%s`!"
          .formatted(userDefined.name));
      resolution.info("ANALYZER", userDefined.definition.portion,
        "Previous decleration was here.");
      return;
    }
    solution.unresolved.put(definition.name.toString(),
      Unresolved.of(definition.name, definition.type, definition.value));
  }
}
