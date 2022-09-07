// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.*;

/** A name and a type: variables or types. Can be built-in or user-defined. */
public sealed abstract class Symbol permits TypeSymbol, Variable {
  /** Built-in symbols. */
  public static final Symbol[] BUILT_IN = { TypeSymbol.META, TypeSymbol.I1,
    TypeSymbol.I2, TypeSymbol.I4, TypeSymbol.I8, TypeSymbol.IX, TypeSymbol.U1,
    TypeSymbol.U2, TypeSymbol.U4, TypeSymbol.U8, TypeSymbol.UX, TypeSymbol.F4,
    TypeSymbol.F8, TypeSymbol.RINF };

  /** String that matches to the symbol. */
  public final String            name;
  /** Lexeme that declares the symbol. Null if the symbol is built-in. */
  public final Lexeme.Identifier declaration;
  /** Type and value. Type is null if the type is the meta type. */
  public final Evaluation        evaluation;

  /** Constructor. */
  protected Symbol(String name, Lexeme.Identifier declaration,
    Evaluation evaluation) {
    this.name        = name;
    this.declaration = declaration;
    this.evaluation  = evaluation;
  }

  @Override
  public String toString() {
    return this.name.toString();
  }
}
