// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

final class HalfSolution {
  static HalfSolution of() {
    return new HalfSolution(new HashMap<>(), new HashMap<>());
  }

  final Map<String, Symbol>     resolved;
  final Map<String, Unresolved> unresolved;

  private HalfSolution(
    Map<String, Symbol> resolved,
    Map<String, Unresolved> unresolved) {
    this.resolved   = resolved;
    this.unresolved = unresolved;
  }
}
