// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.BigDecimal;
import java.util.Objects;

/** Smallest meaningful group of characters in a source. */
public sealed abstract class Lexeme permits Lexeme.Token, Lexeme.Number, Lexeme.Identifier, Lexeme.Keyword {
  /** Non-alphanumeric character group. */
  public static sealed abstract class Token extends
    Lexeme permits Plus, Minus, Star, ForwardSlash, Percent, Equal, Colon, Semicolon, OpeningParentheses, ClosingParentheses, EOF {
    public Token(Portion portion) {
      super(portion);
    }
  }

  /** Plus sign. */
  public static final class Plus extends Token {
    public Plus(Portion portion) {
      super(portion);
    }
  }

  /** Minus sign. */
  public static final class Minus extends Token {
    public Minus(Portion portion) {
      super(portion);
    }
  }

  /** Star. */
  public static final class Star extends Token {
    public Star(Portion portion) {
      super(portion);
    }
  }

  /** Forward slash. */
  public static final class ForwardSlash extends Token {
    public ForwardSlash(Portion portion) {
      super(portion);
    }
  }

  /** Percent symbol. */
  public static final class Percent extends Token {
    public Percent(Portion portion) {
      super(portion);
    }
  }

  /** Equal sign. */
  public static final class Equal extends Token {
    public Equal(Portion portion) {
      super(portion);
    }
  }

  /** Colon. */
  public static final class Colon extends Token {
    public Colon(Portion portion) {
      super(portion);
    }
  }

  /** Semicolon. */
  public static final class Semicolon extends Token {
    public Semicolon(Portion portion) {
      super(portion);
    }
  }

  /** Opening parentheses. */
  public static final class OpeningParentheses extends Token {
    public OpeningParentheses(Portion portion) {
      super(portion);
    }
  }

  /** Closing parentheses. */
  public static final class ClosingParentheses extends Token {
    public ClosingParentheses(Portion portion) {
      super(portion);
    }
  }

  /** End of file character. */
  public static final class EOF extends Token {
    public EOF(Portion portion) {
      super(portion);
    }
  }

  /** Number literal. */
  public static final class Number extends Lexeme {
    /** Value. */
    public final BigDecimal value;

    public Number(Portion portion, BigDecimal value) {
      super(portion);
      this.value = value;
    }

    @Override
    public int hashCode() {
      final int prime  = 31;
      int       result = super.hashCode();
      result = prime * result + Objects.hash(value);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!super.equals(obj)) {
        return false;
      }
      if (!(obj instanceof Number)) {
        return false;
      }
      Number other = (Number) obj;
      return Objects.equals(value, other.value);
    }
  }

  /** Symbol name in definition or reference. */
  public static final class Identifier extends Lexeme {
    /** Value. */
    public final String value;

    public Identifier(Portion portion, String value) {
      super(portion);
      this.value = value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.value);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Identifier other)) {
        return false;
      }
      return Objects.equals(this.value, other.value);
    }
  }

  /** Reserved identifier. */
  public static sealed abstract class Keyword extends
    Lexeme permits I1, I2, I4, I8, IX, U1, U2, U4, U8, UX, F4, F8, Rinf {
    public Keyword(Portion portion) {
      super(portion);
    }

  }

  /** Keyword `i1`; 1 byte, signed integer type. */
  public static final class I1 extends Keyword {
    public I1(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `i2`; 2 byte, signed integer type. */
  public static final class I2 extends Keyword {
    public I2(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `i4`; 4 byte, signed integer type. */
  public static final class I4 extends Keyword {
    public I4(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `i8`; 8 byte, signed integer type. */
  public static final class I8 extends Keyword {
    public I8(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `ix`; pointer size, signed integer type. */
  public static final class IX extends Keyword {
    public IX(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `u1`; 1 byte, unsigned integer type. */
  public static final class U1 extends Keyword {
    public U1(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `u2`; 2 byte, unsigned integer type. */
  public static final class U2 extends Keyword {
    public U2(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `u4`; 4 byte, unsigned integer type. */
  public static final class U4 extends Keyword {
    public U4(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `u8`; 8 byte, unsigned integer type. */
  public static final class U8 extends Keyword {
    public U8(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `ux`; pointer size, unsigned integer type. */
  public static final class UX extends Keyword {
    public UX(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `f4`; 4 byte, floating-point real. */
  public static final class F4 extends Keyword {
    public F4(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `f8`; 8 byte, floating-point real. */
  public static final class F8 extends Keyword {
    public F8(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `rinf`; infinite-precision, compile-time real. */
  public static final class Rinf extends Keyword {
    public Rinf(Portion portion) {
      super(portion);
    }
  }

  /** Portion in the source. */
  public final Portion portion;

  public Lexeme(Portion portion) {
    this.portion = portion;
  }

  @Override
  public String toString() {
    return this.portion.toString();
  }

  @Override
  public int hashCode() {
    return this.getClass().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return this.getClass().equals(obj.getClass());
  }
}
