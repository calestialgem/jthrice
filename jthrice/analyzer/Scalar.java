// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

/** Independent, undivisible, built-in types. */
public sealed abstract class Scalar extends
  Type permits Scalar.Signed, Scalar.Unsigned, Scalar.Floating, Scalar.Rinf {
  /** Type i1. */
  public static final I1   I1   = new I1();
  /** Type i2. */
  public static final I2   I2   = new I2();
  /** Type i4. */
  public static final I4   I4   = new I4();
  /** Type i8. */
  public static final I8   I8   = new I8();
  /** Type ix. */
  public static final Ix   IX   = new Ix();
  /** Type u1. */
  public static final U1   U1   = new U1();
  /** Type u2. */
  public static final U2   U2   = new U2();
  /** Type u4. */
  public static final U4   U4   = new U4();
  /** Type u8. */
  public static final U8   U8   = new U8();
  /** Type ux. */
  public static final Ux   UX   = new Ux();
  /** Type f4. */
  public static final F4   F4   = new F4();
  /** Type f8. */
  public static final F8   F8   = new F8();
  /** Type rinf. */
  public static final Rinf RINF = new Rinf();

  /** Constructor. */
  private Scalar() {
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

    @Override
    public String toString() {
      return "i1";
    }
  }

  /** 2 byte, signed integer. */
  public static final class I2 extends Signed {
    /** Constructor. */
    private I2() {
    }

    @Override
    public String toString() {
      return "i2";
    }
  }

  /** 4 byte, signed integer. */
  public static final class I4 extends Signed {
    /** Constructor. */
    private I4() {
    }

    @Override
    public String toString() {
      return "i4";
    }
  }

  /** 8 byte, signed integer. */
  public static final class I8 extends Signed {
    /** Constructor. */
    private I8() {
    }

    @Override
    public String toString() {
      return "i8";
    }
  }

  /** Pointer size, signed integer. */
  public static final class Ix extends Signed {
    /** Constructor. */
    private Ix() {
    }

    @Override
    public String toString() {
      return "ix";
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

    @Override
    public String toString() {
      return "u1";
    }
  }

  /** 2 byte, unsigned integer. */
  public static final class U2 extends Unsigned {
    /** Constructor. */
    private U2() {
    }

    @Override
    public String toString() {
      return "u2";
    }
  }

  /** 4 byte, unsigned integer. */
  public static final class U4 extends Unsigned {
    /** Constructor. */
    private U4() {
    }

    @Override
    public String toString() {
      return "u4";
    }
  }

  /** 8 byte, unsigned integer. */
  public static final class U8 extends Unsigned {
    /** Constructor. */
    private U8() {
    }

    @Override
    public String toString() {
      return "u8";
    }
  }

  /** Pointer size, unsigned integer. */
  public static final class Ux extends Unsigned {
    /** Constructor. */
    private Ux() {
    }

    @Override
    public String toString() {
      return "ux";
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

    @Override
    public String toString() {
      return "f4";
    }
  }

  /** 8 byte, floating-point real. */
  public static final class F8 extends Floating {
    /** Constructor. */
    private F8() {
    }

    @Override
    public String toString() {
      return "f8";
    }
  }

  /** Infinite-precision, compile-time real. Type of any number literal. */
  public static final class Rinf extends Scalar {
    /** Constructor. */
    private Rinf() {
    }

    @Override
    public String toString() {
      return "rinf";
    }
  }
}
