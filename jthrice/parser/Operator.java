// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import jthrice.lexer.*;

sealed abstract class Operator permits Nofix, Prefix, Postfix, Cirfix, Infix, Polifix {
  private static Operator[] PRIMARY = { Nofix.of(Decimal.class),
    Nofix.of(Identifier.class), Nofix.of(I1.class), Nofix.of(I2.class),
    Nofix.of(I4.class), Nofix.of(I8.class), Nofix.of(Ix.class),
    Nofix.of(U1.class), Nofix.of(U2.class), Nofix.of(U4.class),
    Nofix.of(U8.class), Nofix.of(Ux.class), Nofix.of(F4.class),
    Nofix.of(F8.class),
    Cirfix.of(OpeningParenthesis.class, ClosingParenthesis.class) };

  private static Operator[] UNARY = { Prefix.of(Plus.class),
    Prefix.of(Minus.class) };

  private static Operator[] FACTOR = { Infix.of(Star.class),
    Infix.of(Slash.class), Infix.of(Percent.class) };

  private static Operator[] TERM = { Infix.of(Plus.class),
    Infix.of(Minus.class) };

  private static Operator[][] ORDER = { TERM, FACTOR, UNARY, PRIMARY };

  static int length(int precedence) {
    return ORDER[precedence].length;
  }

  static int precedences() {
    return ORDER.length;
  }

  static Operator get(int precedence, int index) {
    return ORDER[precedence][index];
  }
}
