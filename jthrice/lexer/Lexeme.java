// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.*;

/** Smallest meaningful group of characters in a source. */
public sealed abstract class Lexeme permits Lexeme.Token, Lexeme.Number, Lexeme.Identifier, Lexeme.Keyword {
  /** Non-alphanumeric character group. */
  public static sealed abstract class Token extends
    Lexeme permits Plus, Minus, Star, ForwardSlash, Percent, Equal, Colon, Semicolon, OpeningParentheses, ClosingParentheses, EOF {
    /** Constructor. */
    Token(Portion portion) {
      super(portion);
    }
  }

  /** Plus sign. */
  public static final class Plus extends Token {
    /** Constructor. */
    Plus(Portion portion) {
      super(portion);
    }
  }

  /** Minus sign. */
  public static final class Minus extends Token {
    /** Constructor. */
    Minus(Portion portion) {
      super(portion);
    }
  }

  /** Star. */
  public static final class Star extends Token {
    /** Constructor. */
    Star(Portion portion) {
      super(portion);
    }
  }

  /** Forward slash. */
  public static final class ForwardSlash extends Token {
    /** Constructor. */
    ForwardSlash(Portion portion) {
      super(portion);
    }
  }

  /** Percent symbol. */
  public static final class Percent extends Token {
    /** Constructor. */
    Percent(Portion portion) {
      super(portion);
    }
  }

  /** Equal sign. */
  public static final class Equal extends Token {
    /** Constructor. */
    Equal(Portion portion) {
      super(portion);
    }
  }

  /** Colon. */
  public static final class Colon extends Token {
    /** Constructor. */
    Colon(Portion portion) {
      super(portion);
    }
  }

  /** Semicolon. */
  public static final class Semicolon extends Token {
    /** Constructor. */
    Semicolon(Portion portion) {
      super(portion);
    }
  }

  /** Opening parentheses. */
  public static final class OpeningParentheses extends Token {
    /** Constructor. */
    OpeningParentheses(Portion portion) {
      super(portion);
    }
  }

  /** Closing parentheses. */
  public static final class ClosingParentheses extends Token {
    /** Constructor. */
    ClosingParentheses(Portion portion) {
      super(portion);
    }
  }

  /** End of file character. */
  public static final class EOF extends Token {
    /** Constructor. */
    EOF(Portion portion) {
      super(portion);
    }
  }

  /** Number literal. */
  public static final class Number extends Lexeme {
    /** Value. */
    public final BigDecimal value;

    /** Constructor. */
    Number(Portion portion, BigDecimal value) {
      super(portion);
      this.value = value;
    }
  }

  /** Symbol name in definition or reference. */
  public static final class Identifier extends Lexeme {
    /** Value. */
    public final String value;

    /** Constructor. */
    Identifier(Portion portion, String value) {
      super(portion);
      this.value = value;
    }
  }

  /** Reserved identifier. */
  public static sealed abstract class Keyword extends
    Lexeme permits I1, I2, I4, I8, Ix, U1, U2, U4, U8, Ux, F4, F8, Rinf {
    /** Constructor. */
    Keyword(Portion portion) {
      super(portion);
    }

  }

  /** Keyword `i1`; 1 byte, signed integer type. */
  public static final class I1 extends Keyword {
    /** Constructor. */
    I1(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `i2`; 2 byte, signed integer type. */
  public static final class I2 extends Keyword {
    /** Constructor. */
    I2(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `i4`; 4 byte, signed integer type. */
  public static final class I4 extends Keyword {
    /** Constructor. */
    I4(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `i8`; 8 byte, signed integer type. */
  public static final class I8 extends Keyword {
    /** Constructor. */
    I8(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `ix`; pointer size, signed integer type. */
  public static final class Ix extends Keyword {
    /** Constructor. */
    Ix(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `u1`; 1 byte, unsigned integer type. */
  public static final class U1 extends Keyword {
    /** Constructor. */
    U1(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `u2`; 2 byte, unsigned integer type. */
  public static final class U2 extends Keyword {
    /** Constructor. */
    U2(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `u4`; 4 byte, unsigned integer type. */
  public static final class U4 extends Keyword {
    /** Constructor. */
    U4(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `u8`; 8 byte, unsigned integer type. */
  public static final class U8 extends Keyword {
    /** Constructor. */
    U8(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `ux`; pointer size, unsigned integer type. */
  public static final class Ux extends Keyword {
    /** Constructor. */
    Ux(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `f4`; 4 byte, floating-point real. */
  public static final class F4 extends Keyword {
    /** Constructor. */
    F4(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `f8`; 8 byte, floating-point real. */
  public static final class F8 extends Keyword {
    /** Constructor. */
    F8(Portion portion) {
      super(portion);
    }
  }

  /** Keyword `rinf`; infinite-precision, compile-time real. */
  public static final class Rinf extends Keyword {
    /** Constructor. */
    Rinf(Portion portion) {
      super(portion);
    }
  }

  /** Portion in the source. */
  public final Portion portion;

  /** Constructor. */
  Lexeme(Portion portion) {
    this.portion = portion;
  }

  @Override
  public String toString() {
    return this.portion.toString();
  }
}
