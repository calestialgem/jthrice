// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

public final class Solution {
  static Solution of(Map<String, Symbol> symbols) {
    return new Solution(symbols);
  }

  public final Map<String, Symbol> symbols;

  private Solution(Map<String, Symbol> symbols) {
    this.symbols = symbols;
  }
}
