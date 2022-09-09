// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.*;

import jthrice.launcher.*;

public sealed abstract class Lexeme permits Lexeme.Token, Lexeme.Number, Lexeme.Identifier, Lexeme.Keyword {
  public static String toString(Class<? extends Lexeme> type) {
    if (type.equals(Plus.class)) {
      return "+";
    }
    if (type.equals(Minus.class)) {
      return "-";
    }
    if (type.equals(Star.class)) {
      return "*";
    }
    if (type.equals(ForwardSlash.class)) {
      return "/";
    }
    if (type.equals(Percent.class)) {
      return "%";
    }
    if (type.equals(Equal.class)) {
      return "=";
    }
    if (type.equals(Colon.class)) {
      return ":";
    }
    if (type.equals(Semicolon.class)) {
      return ";";
    }
    if (type.equals(OpeningParentheses.class)) {
      return "(";
    }
    if (type.equals(ClosingParentheses.class)) {
      return ")";
    }
    if (type.equals(EOF.class)) {
      return "" + Source.EOF;
    }
    if (type.equals(Number.class)) {
      return "number";
    }
    if (type.equals(Identifier.class)) {
      return "identifier";
    }
    if (type.equals(I1.class)) {
      return "i1";
    }
    if (type.equals(I2.class)) {
      return "i2";
    }
    if (type.equals(I4.class)) {
      return "i4";
    }
    if (type.equals(I8.class)) {
      return "i8";
    }
    if (type.equals(Ix.class)) {
      return "ix";
    }
    if (type.equals(U1.class)) {
      return "u1";
    }
    if (type.equals(U2.class)) {
      return "u2";
    }
    if (type.equals(U4.class)) {
      return "u4";
    }
    if (type.equals(U8.class)) {
      return "u8";
    }
    if (type.equals(Ux.class)) {
      return "ux";
    }
    if (type.equals(F4.class)) {
      return "f4";
    }
    if (type.equals(F8.class)) {
      return "f8";
    }
    if (type.equals(Rinf.class)) {
      return "rinf";
    }
    if (type.equals(Type.class)) {
      return "type";
    }
    throw new RuntimeException("Unknown lexeme type: %s!".formatted(type));
  }

  public static sealed abstract class Token extends
    Lexeme permits Plus, Minus, Star, ForwardSlash, Percent, Equal, Colon, Semicolon, OpeningParentheses, ClosingParentheses, EOF {
    Token(Portion portion) {
      super(portion);
    }
  }

  public static final class Plus extends Token {
    Plus(Portion portion) {
      super(portion);
    }
  }

  public static final class Minus extends Token {
    Minus(Portion portion) {
      super(portion);
    }
  }

  public static final class Star extends Token {
    Star(Portion portion) {
      super(portion);
    }
  }

  public static final class ForwardSlash extends Token {
    ForwardSlash(Portion portion) {
      super(portion);
    }
  }

  public static final class Percent extends Token {
    Percent(Portion portion) {
      super(portion);
    }
  }

  public static final class Equal extends Token {
    Equal(Portion portion) {
      super(portion);
    }
  }

  public static final class Colon extends Token {
    Colon(Portion portion) {
      super(portion);
    }
  }

  public static final class Semicolon extends Token {
    Semicolon(Portion portion) {
      super(portion);
    }
  }

  public static final class OpeningParentheses extends Token {
    OpeningParentheses(Portion portion) {
      super(portion);
    }
  }

  public static final class ClosingParentheses extends Token {
    ClosingParentheses(Portion portion) {
      super(portion);
    }
  }

  public static final class EOF extends Token {
    EOF(Portion portion) {
      super(portion);
    }
  }

  public static final class Number extends Lexeme {
    public final BigDecimal value;

    Number(Portion portion, BigDecimal value) {
      super(portion);
      this.value = value;
    }
  }

  public static final class Identifier extends Lexeme {
    public final String value;

    Identifier(Portion portion, String value) {
      super(portion);
      this.value = value;
    }
  }

  public static sealed abstract class Keyword extends
    Lexeme permits I1, I2, I4, I8, Ix, U1, U2, U4, U8, Ux, F4, F8, Rinf, Type {
    Keyword(Portion portion) {
      super(portion);
    }
  }

  public static final class I1 extends Keyword {
    I1(Portion portion) {
      super(portion);
    }
  }

  public static final class I2 extends Keyword {
    I2(Portion portion) {
      super(portion);
    }
  }

  public static final class I4 extends Keyword {
    I4(Portion portion) {
      super(portion);
    }
  }

  public static final class I8 extends Keyword {
    I8(Portion portion) {
      super(portion);
    }
  }

  public static final class Ix extends Keyword {
    Ix(Portion portion) {
      super(portion);
    }
  }

  public static final class U1 extends Keyword {
    U1(Portion portion) {
      super(portion);
    }
  }

  public static final class U2 extends Keyword {
    U2(Portion portion) {
      super(portion);
    }
  }

  public static final class U4 extends Keyword {
    U4(Portion portion) {
      super(portion);
    }
  }

  public static final class U8 extends Keyword {
    U8(Portion portion) {
      super(portion);
    }
  }

  public static final class Ux extends Keyword {
    Ux(Portion portion) {
      super(portion);
    }
  }

  public static final class F4 extends Keyword {
    F4(Portion portion) {
      super(portion);
    }
  }

  public static final class F8 extends Keyword {
    F8(Portion portion) {
      super(portion);
    }
  }

  public static final class Rinf extends Keyword {
    Rinf(Portion portion) {
      super(portion);
    }
  }

  public static final class Type extends Keyword {
    Type(Portion portion) {
      super(portion);
    }
  }

  public final Portion portion;

  Lexeme(Portion portion) {
    this.portion = portion;
  }

  @Override
  public String toString() {
    return this.portion.toString();
  }
}
