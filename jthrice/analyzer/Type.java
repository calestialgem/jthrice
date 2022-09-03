// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

/** Type of a value. */
public sealed abstract class Type permits Type.Scalar, Type.Meta {
  /** 1 byte, signed integer type. */
  public static final Type I1   = new I1();
  /** 2 byte, signed integer type. */
  public static final Type I2   = new I2();
  /** 4 byte, signed integer type. */
  public static final Type I4   = new I4();
  /** 8 byte, signed integer type. */
  public static final Type I8   = new I8();
  /** Pointer size, signed integer type. */
  public static final Type IX   = new IX();
  /** 1 byte, unsigned integer type. */
  public static final Type U1   = new U1();
  /** 2 byte, unsigned integer type. */
  public static final Type U2   = new U2();
  /** 4 byte, unsigned integer type. */
  public static final Type U4   = new U4();
  /** 8 byte, unsigned integer type. */
  public static final Type U8   = new U8();
  /** Pointer size, unsigned integer type. */
  public static final Type UX   = new UX();
  /** 4 byte, floating-point real type. */
  public static final Type F4   = new F4();
  /** 8 byte, floating-point real type. */
  public static final Type F8   = new F8();
  /** Meta type; type type. */
  public static final Type META = new Meta();

  /** Independent, undivisible, built-in types. */
  public static sealed abstract class Scalar
    extends Type permits Signed, Unsigned, Floating {
  }

  /** Integers with a bit for signedness. */
  public static sealed abstract class Signed
    extends Scalar permits I1, I2, I4, I8, IX {
  }

  /** 1 byte, signed integer. */
  public static final class I1 extends Signed {
  }

  /** 2 byte, signed integer. */
  public static final class I2 extends Signed {
  }

  /** 4 byte, signed integer. */
  public static final class I4 extends Signed {
  }

  /** 8 byte, signed integer. */
  public static final class I8 extends Signed {
  }

  /** Pointer size, signed integer. */
  public static final class IX extends Signed {
  }

  /** Integers without a sign bit. */
  public static sealed abstract class Unsigned
    extends Scalar permits U1, U2, U4, U8, UX {
  }

  /** 1 byte, unsigned integer. */
  public static final class U1 extends Unsigned {
  }

  /** 2 byte, unsigned integer. */
  public static final class U2 extends Unsigned {
  }

  /** 4 byte, unsigned integer. */
  public static final class U4 extends Unsigned {
  }

  /** 8 byte, unsigned integer. */
  public static final class U8 extends Unsigned {
  }

  /** Pointer size, unsigned integer. */
  public static final class UX extends Unsigned {
  }

  /**
   * Real numbers stored as sign, fraction and exponent such that the position
   * of the decimal point can vary.
   */
  public static sealed abstract class Floating extends Scalar permits F4, F8 {
  }

  /** 4 byte, floating-point real. */
  public static final class F4 extends Floating {
  }

  /** 8 byte, floating-point real. */
  public static final class F8 extends Floating {
  }

  /** Meta type; type type. Type of any expression that results in a type. */
  public static final class Meta extends Type {
  }
}
