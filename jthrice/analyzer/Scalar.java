// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.*;

/** Independent, undivisible, built-in types. */
public sealed abstract class Scalar extends
  Type permits Scalar.Signed, Scalar.Unsigned, Scalar.Floating, Scalar.Rinf {
  /** Type i1. */
  public static final I1   I1   = new I1("i1", null, Type.META);
  /** Type i2. */
  public static final I2   I2   = new I2("i2", null, Type.META);
  /** Type i4. */
  public static final I4   I4   = new I4("i4", null, Type.META);
  /** Type i8. */
  public static final I8   I8   = new I8("i8", null, Type.META);
  /** Type ix. */
  public static final Ix   IX   = new Ix("ix", null, Type.META);
  /** Type u1. */
  public static final U1   U1   = new U1("u1", null, Type.META);
  /** Type u2. */
  public static final U2   U2   = new U2("u2", null, Type.META);
  /** Type u4. */
  public static final U4   U4   = new U4("u4", null, Type.META);
  /** Type u8. */
  public static final U8   U8   = new U8("u8", null, Type.META);
  /** Type ux. */
  public static final Ux   UX   = new Ux("ux", null, Type.META);
  /** Type f4. */
  public static final F4   F4   = new F4("f4", null, Type.META);
  /** Type f8. */
  public static final F8   F8   = new F8("f8", null, Type.META);
  /** Type rinf. */
  public static final Rinf RINF = new Rinf("rinf", null, Type.META);

  /** Constructor. */
  private Scalar(String name, Lexeme.Identifier definition, Type type) {
    super(name, definition, type);
  }

  /** Integers with a bit for signedness. */
  public static sealed abstract class Signed
    extends Scalar permits I1, I2, I4, I8, Ix {
    /** Constructor. */
    private Signed(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 1 byte, signed integer. */
  public static final class I1 extends Signed {
    /** Constructor. */
    private I1(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 2 byte, signed integer. */
  public static final class I2 extends Signed {
    /** Constructor. */
    private I2(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 4 byte, signed integer. */
  public static final class I4 extends Signed {
    /** Constructor. */
    private I4(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 8 byte, signed integer. */
  public static final class I8 extends Signed {
    /** Constructor. */
    private I8(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Pointer size, signed integer. */
  public static final class Ix extends Signed {
    /** Constructor. */
    private Ix(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Integers without a sign bit. */
  public static sealed abstract class Unsigned
    extends Scalar permits U1, U2, U4, U8, Ux {
    /** Constructor. */
    private Unsigned(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 1 byte, unsigned integer. */
  public static final class U1 extends Unsigned {
    /** Constructor. */
    private U1(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 2 byte, unsigned integer. */
  public static final class U2 extends Unsigned {
    /** Constructor. */
    private U2(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 4 byte, unsigned integer. */
  public static final class U4 extends Unsigned {
    /** Constructor. */
    private U4(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 8 byte, unsigned integer. */
  public static final class U8 extends Unsigned {
    /** Constructor. */
    private U8(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Pointer size, unsigned integer. */
  public static final class Ux extends Unsigned {
    /** Constructor. */
    private Ux(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Real numbers stored as sign, fraction and exponent such that the position
   * of the decimal point can vary. */
  public static sealed abstract class Floating extends Scalar permits F4, F8 {
    /** Constructor. */
    private Floating(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 4 byte, floating-point real. */
  public static final class F4 extends Floating {
    /** Constructor. */
    private F4(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 8 byte, floating-point real. */
  public static final class F8 extends Floating {
    /** Constructor. */
    private F8(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Infinite-precision, compile-time real. Type of any number literal. */
  public static final class Rinf extends Scalar {
    /** Constructor. */
    private Rinf(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }
}
