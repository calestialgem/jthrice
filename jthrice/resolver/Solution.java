// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.resolver;

import java.util.*;

import jthrice.lexer.*;

/** Type and variable information of a Thrice program. */
public class Solution {
  /** Clean solution. */
  static Solution of() {
    return new Solution(new HashMap<>(), new HashMap<>());
  }

  /** Mapping from type names to types. */
  public final Map<String, Type>            types;
  /** Mapping from variable names to variable types. */
  public final Map<Lexeme.Identifier, Type> variables;

  /** Constructor. */
  private Solution(Map<String, Type> types,
    Map<Lexeme.Identifier, Type> variables) {
    this.types     = types;
    this.variables = variables;
  }
}
