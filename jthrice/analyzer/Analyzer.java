// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.launcher.*;
import jthrice.parser.*;

/** Analyzes a solution from a program node. */
public final class Analyzer {
  /** Analyze the given program node and report to the given resolution. */
  public static Solution analyze(Resolution resolution, Node.Program program) {
    var halfSolution = HalfSolution.of();
    for (var symbol : Symbol.BUILT_IN) {
      halfSolution.resolved.put(symbol.name, symbol);
    }
    FirstPass.resolve(resolution, halfSolution, program);
    SecondPass.resolve(resolution, halfSolution, program);
    ThirdPass.resolve(resolution, halfSolution, program);
    FourthPass.resolve(resolution, halfSolution, program);
    if (!halfSolution.unresolved.isEmpty()) {
      resolution.error("ANALYZER", "There were unresolved symbols!");
      return null;
    }
    return Solution.of(halfSolution.resolved);
  }
}
