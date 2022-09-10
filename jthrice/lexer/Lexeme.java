// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import jthrice.launcher.*;

public sealed abstract class Lexeme permits Token, Keyword, Decimal, Identifier, Unknown {
  public static String toString(Class<? extends Lexeme> type) {
    if (type == Equal.class) {
      return "=";
    }
    if (type == Colon.class) {
      return ":";
    }
    if (type == Semicolon.class) {
      return ";";
    }
    if (type == OpeningParenthesis.class) {
      return "(";
    }
    if (type == ClosingParenthesis.class) {
      return ")";
    }
    if (type == Plus.class) {
      return "+";
    }
    if (type == Minus.class) {
      return "-";
    }
    if (type == Star.class) {
      return "*";
    }
    if (type == Slash.class) {
      return "/";
    }
    if (type == Percent.class) {
      return "%";
    }
    if (type == EOF.class) {
      return "eof";
    }
    if (type == I1.class) {
      return "i1";
    }
    if (type == I2.class) {
      return "i2";
    }
    if (type == I4.class) {
      return "i4";
    }
    if (type == I8.class) {
      return "i8";
    }
    if (type == Ix.class) {
      return "ix";
    }
    if (type == U1.class) {
      return "u1";
    }
    if (type == U2.class) {
      return "u2";
    }
    if (type == U4.class) {
      return "u4";
    }
    if (type == U8.class) {
      return "u8";
    }
    if (type == Ux.class) {
      return "ux";
    }
    if (type == F4.class) {
      return "f4";
    }
    if (type == F8.class) {
      return "f8";
    }
    if (type == Decimal.class) {
      return "decimal";
    }
    if (type == Identifier.class) {
      return "identifier";
    }
    if (type == Unknown.class) {
      return "unknown";
    }
    return null;
  }

  public final Portion portion;

  Lexeme(Portion portion) {
    this.portion = portion;
  }

  @Override
  public String toString() {
    return portion.toString();
  }
}
