// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

import jthrice.launcher.*;
import jthrice.parser.*;

public final class Analyzer {
  public static Solution analyze(Resolution resolution, Root root) {
    var halfSolution = HalfSolution.of();
    addBuiltIn(halfSolution);

    FirstPass.resolve(resolution, halfSolution, root);
    SecondPass.resolve(resolution, halfSolution, root);
    ThirdPass.resolve(resolution, halfSolution, root);
    FourthPass.resolve(resolution, halfSolution, root);
    if (!halfSolution.unresolved.isEmpty()) {
      resolution.error("ANALYZER", "There were unresolved symbols!");
      return null;
    }
    return Solution.of(halfSolution.resolved);
  }

  private static void addBuiltIn(HalfSolution solution) {
    for (var symbol : Symbol.BUILT_IN) {
      solution.resolved.put(symbol.name, symbol);
    }
  }
}
