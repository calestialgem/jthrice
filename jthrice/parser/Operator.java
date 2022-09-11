// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import jthrice.lexer.*;

public sealed abstract class Operator permits NullaryOperator, PrenaryOperator, PostaryOperator, CirnaryOperator, BinaryOperator, VariaryOperator {
  public static final NullaryOperator DECIMAL    = NullaryOperator
    .of(Decimal.class);
  public static final NullaryOperator IDENTIFIER = NullaryOperator
    .of(Identifier.class);
  public static final NullaryOperator I1         = NullaryOperator
    .of(I1.class);
  public static final NullaryOperator I2         = NullaryOperator
    .of(I2.class);
  public static final NullaryOperator I4         = NullaryOperator
    .of(I4.class);
  public static final NullaryOperator I8         = NullaryOperator
    .of(I8.class);
  public static final NullaryOperator IX         = NullaryOperator
    .of(Ix.class);
  public static final NullaryOperator U1         = NullaryOperator
    .of(U1.class);
  public static final NullaryOperator U2         = NullaryOperator
    .of(U2.class);
  public static final NullaryOperator U4         = NullaryOperator
    .of(U4.class);
  public static final NullaryOperator U8         = NullaryOperator
    .of(U8.class);
  public static final NullaryOperator UX         = NullaryOperator
    .of(Ux.class);
  public static final NullaryOperator F4         = NullaryOperator
    .of(F4.class);
  public static final NullaryOperator F8         = NullaryOperator
    .of(F8.class);
  public static final CirnaryOperator GROUP      = CirnaryOperator
    .of(OpeningParenthesis.class, ClosingParenthesis.class);
  public static final PrenaryOperator POSATE     = PrenaryOperator
    .of(Plus.class);
  public static final PrenaryOperator NEGATE     = PrenaryOperator
    .of(Minus.class);
  public static final BinaryOperator  MULTIPLY   = BinaryOperator
    .of(Star.class);
  public static final BinaryOperator  DIVIDE     = BinaryOperator
    .of(Slash.class);
  public static final BinaryOperator  REMINDER   = BinaryOperator
    .of(Percent.class);
  public static final BinaryOperator  ADD        = BinaryOperator
    .of(Plus.class);
  public static final BinaryOperator  SUBTRACT   = BinaryOperator
    .of(Minus.class);

  public static Operator[]   PRIMARY = { DECIMAL, IDENTIFIER, I1, I2, I4, I8,
    IX, U1, U2, U4, U8, UX, F4, F8, GROUP };
  public static Operator[]   UNARY   = { POSATE, NEGATE };
  public static Operator[]   FACTOR  = { MULTIPLY, DIVIDE, REMINDER };
  public static Operator[]   TERM    = { ADD, SUBTRACT };
  public static Operator[][] ORDER   = { TERM, FACTOR, UNARY, PRIMARY };

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
