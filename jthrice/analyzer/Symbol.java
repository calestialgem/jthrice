// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.*;

public sealed abstract class Symbol permits TypeSymbol, Variable {
  public static final Symbol[] BUILT_IN = { TypeSymbol.META, TypeSymbol.I1,
    TypeSymbol.I2, TypeSymbol.I4, TypeSymbol.I8, TypeSymbol.IX, TypeSymbol.U1,
    TypeSymbol.U2, TypeSymbol.U4, TypeSymbol.U8, TypeSymbol.UX, TypeSymbol.F4,
    TypeSymbol.F8, TypeSymbol.RINF };

  public final String     name;
  public final Identifier declaration;
  public final Evaluation evaluation;

  protected Symbol(String name, Identifier declaration,
    Evaluation evaluation) {
    this.name        = name;
    this.declaration = declaration;
    this.evaluation  = evaluation;
  }

  @Override
  public String toString() {
    return name.toString();
  }
}
