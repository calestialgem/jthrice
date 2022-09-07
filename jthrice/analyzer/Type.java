// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.*;

/** Type of a value. */
public sealed abstract class Type extends Symbol permits Type.Meta, Scalar {
  /** Type meta; type of types. */
  public static final Meta META = new Meta("type", null, null);

  /** Constructor. */
  protected Type(String name, Lexeme.Identifier definition, Type type) {
    super(name, definition, type);
  }

  /** Meta type; type type. Type of any expression that results in a type. */
  public static final class Meta extends Type {
    /** Constructor. */
    private Meta(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }
}
