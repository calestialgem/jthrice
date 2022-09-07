// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

/** All the built-in and user-defined symbols in a program. */
public final class Solution {
  /** Solution of the given symbols. */
  static Solution of(Map<String, Symbol> symbols) {
    return new Solution(symbols);
  }

  /** Symbols. */
  public final Map<String, Symbol> symbols;

  /** Constructor. */
  private Solution(Map<String, Symbol> symbols) {
    this.symbols = symbols;
  }
}
