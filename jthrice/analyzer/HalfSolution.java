// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

/** Intermidiate result of a pass. Some of the symbols are resolved, some are
 * unresolved. */
final class HalfSolution {
  /** Clean solution. */
  static HalfSolution of() {
    return new HalfSolution(new HashMap<>(), new HashMap<>());
  }

  /** Resolved symbols. */
  public final Map<String, Symbol>     resolved;
  /** Unresolved symbols. */
  public final Map<String, Unresolved> unresolved;

  /** Constructor. */
  private HalfSolution(Map<String, Symbol> resolved,
    Map<String, Unresolved> unresolved) {
    this.resolved   = resolved;
    this.unresolved = unresolved;
  }
}
