// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer.first;

import java.util.*;

import jthrice.analyzer.*;

/** Result of the first pass. Stores the built-in symbols and unresolved form of
 * custom-defined ones. */
public final class FirstSolution {
  /** Solution of the given symbols. */
  static FirstSolution of(Map<String, Symbol> symbols,
    Map<String, Unresolved> unresolveds) {
    return new FirstSolution(symbols, unresolveds);
  }

  /** Built-in symbols. */
  public final Map<String, Symbol>     symbols;
  /** User-defined symbols. */
  public final Map<String, Unresolved> unresolveds;

  /** Constructor. */
  private FirstSolution(Map<String, Symbol> symbols,
    Map<String, Unresolved> unresolveds) {
    this.symbols     = symbols;
    this.unresolveds = unresolveds;
  }
}
