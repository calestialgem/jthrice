// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import jthrice.launcher.*;

public sealed abstract class Lexeme permits Token, Keyword, Decimal, Identifier, Unknown {
  public final Portion portion;

  Lexeme(Portion portion) {
    this.portion = portion;
  }

  @Override
  public String toString() {
    return portion.toString();
  }
}
