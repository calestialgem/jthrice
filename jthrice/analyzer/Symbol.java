// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.*;

/** A name and a type: variables or types. Can be built-in or user-defined. */
public sealed abstract class Symbol permits Type {
  /** Built-in symbols. */
  public static final Symbol[] BUILT_IN = { Type.META, Scalar.I1, Scalar.I2,
    Scalar.I4, Scalar.I8, Scalar.IX, Scalar.U1, Scalar.U2, Scalar.U4, Scalar.U8,
    Scalar.UX, Scalar.F4, Scalar.F8, Scalar.RINF };

  /** String that matches to the symbol. */
  public final String            name;
  /** Lexeme that defines the symbol. Null if the symbol is built-in. */
  public final Lexeme.Identifier definition;
  /** Type of the symbol. Null if the type is the meta type, type of types. */
  public final Type              type;

  /** Constructor. */
  protected Symbol(String name, Lexeme.Identifier definition, Type type) {
    this.name       = name;
    this.definition = definition;
    this.type       = type;
  }

  @Override
  public String toString() {
    return this.name.toString();
  }
}
