// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

/** Type of a value. */
public sealed abstract class Type permits Type.Meta, Scalar {
  /** Type meta; type of types. */
  public static final Meta META = new Meta();

  /** Constructor. */
  protected Type() {
  }

  /** Meta type; type type. Type of any expression that results in a type. */
  public static final class Meta extends Type {
    /** Constructor. */
    private Meta() {
    }

    @Override
    public String toString() {
      return "type";
    }
  }
}
