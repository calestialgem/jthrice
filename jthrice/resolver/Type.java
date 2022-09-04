// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.resolver;

/** Type of a value. */
public sealed abstract class Type permits Type.Scalar, Type.Meta {
  /** Independent, undivisible, built-in types. */
  public static sealed abstract class Scalar
    extends Type permits Signed, Unsigned, Floating, Rinf {
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

  /** Real numbers stored as sign, fraction and exponent such that the position
   * of the decimal point can vary. */
  public static sealed abstract class Floating extends Scalar permits F4, F8 {
  }

  /** 4 byte, floating-point real. */
  public static final class F4 extends Floating {
  }

  /** 8 byte, floating-point real. */
  public static final class F8 extends Floating {
  }

  /** Infinite-precision, compile-time real. Type of any number literal. */
  public static final class Rinf extends Scalar {
  }

  /** Meta type; type type. Type of any expression that results in a type. */
  public static final class Meta extends Type {
  }
}
