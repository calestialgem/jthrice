// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.resolver;

/** Type of a value. */
public sealed abstract class Type permits Type.Scalar, Type.Meta {
  /** Type i1. */
  static Type ofI1() {
    return new I1();
  }

  /** Type i2. */
  static Type ofI2() {
    return new I2();
  }

  /** Type i4. */
  static Type ofI4() {
    return new I4();
  }

  /** Type i8. */
  static Type ofI8() {
    return new I8();
  }

  /** Type ix. */
  static Type ofIx() {
    return new Ix();
  }

  /** Type u1. */
  static Type ofU1() {
    return new U1();
  }

  /** Type u2. */
  static Type ofU2() {
    return new U2();
  }

  /** Type u4. */
  static Type ofU4() {
    return new U4();
  }

  /** Type u8. */
  static Type ofU8() {
    return new U8();
  }

  /** Type ux. */
  static Type ofUx() {
    return new Ux();
  }

  /** Type f4. */
  static Type ofF4() {
    return new F4();
  }

  /** Type f8. */
  static Type ofF8() {
    return new F8();
  }

  /** Type rinf. */
  static Type ofRinf() {
    return new Rinf();
  }

  /** Type meta. */
  static Type ofMeta() {
    return new Meta();
  }

  /** Independent, undivisible, built-in types. */
  public static sealed abstract class Scalar
    extends Type permits Signed, Unsigned, Floating, Rinf {
    /** Constructor. */
    private Scalar() {
    }
  }

  /** Integers with a bit for signedness. */
  public static sealed abstract class Signed
    extends Scalar permits I1, I2, I4, I8, Ix {
    /** Constructor. */
    private Signed() {
    }
  }

  /** 1 byte, signed integer. */
  public static final class I1 extends Signed {
    /** Constructor. */
    private I1() {
    }
  }

  /** 2 byte, signed integer. */
  public static final class I2 extends Signed {
    /** Constructor. */
    private I2() {
    }
  }

  /** 4 byte, signed integer. */
  public static final class I4 extends Signed {
    /** Constructor. */
    private I4() {
    }
  }

  /** 8 byte, signed integer. */
  public static final class I8 extends Signed {
    /** Constructor. */
    private I8() {
    }
  }

  /** Pointer size, signed integer. */
  public static final class Ix extends Signed {
    /** Constructor. */
    private Ix() {
    }
  }

  /** Integers without a sign bit. */
  public static sealed abstract class Unsigned
    extends Scalar permits U1, U2, U4, U8, Ux {
    /** Constructor. */
    private Unsigned() {
    }
  }

  /** 1 byte, unsigned integer. */
  public static final class U1 extends Unsigned {
    /** Constructor. */
    private U1() {
    }
  }

  /** 2 byte, unsigned integer. */
  public static final class U2 extends Unsigned {
    /** Constructor. */
    private U2() {
    }
  }

  /** 4 byte, unsigned integer. */
  public static final class U4 extends Unsigned {
    /** Constructor. */
    private U4() {
    }
  }

  /** 8 byte, unsigned integer. */
  public static final class U8 extends Unsigned {
    /** Constructor. */
    private U8() {
    }
  }

  /** Pointer size, unsigned integer. */
  public static final class Ux extends Unsigned {
    /** Constructor. */
    private Ux() {
    }
  }

  /** Real numbers stored as sign, fraction and exponent such that the position
   * of the decimal point can vary. */
  public static sealed abstract class Floating extends Scalar permits F4, F8 {
    /** Constructor. */
    private Floating() {
    }
  }

  /** 4 byte, floating-point real. */
  public static final class F4 extends Floating {
    /** Constructor. */
    private F4() {
    }
  }

  /** 8 byte, floating-point real. */
  public static final class F8 extends Floating {
    /** Constructor. */
    private F8() {
    }
  }

  /** Infinite-precision, compile-time real. Type of any number literal. */
  public static final class Rinf extends Scalar {
    /** Constructor. */
    private Rinf() {
    }
  }

  /** Meta type; type type. Type of any expression that results in a type. */
  public static final class Meta extends Type {
    /** Constructor. */
    private Meta() {
    }
  }

  /** Constructor. */
  private Type() {
  }
}
