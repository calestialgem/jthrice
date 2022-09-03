// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

/** Type of a value. */
public sealed abstract class Type permits Type.Scalar, Type.Meta {
  public static final Type I1   = new Scalar.Signed.I1();
  public static final Type I2   = new Scalar.Signed.I2();
  public static final Type I4   = new Scalar.Signed.I4();
  public static final Type I8   = new Scalar.Signed.I8();
  public static final Type IX   = new Scalar.Signed.IX();
  public static final Type U1   = new Scalar.Unsigned.U1();
  public static final Type U2   = new Scalar.Unsigned.U2();
  public static final Type U4   = new Scalar.Unsigned.U4();
  public static final Type U8   = new Scalar.Unsigned.U8();
  public static final Type UX   = new Scalar.Unsigned.UX();
  public static final Type F4   = new Scalar.Floating.F4();
  public static final Type F8   = new Scalar.Floating.F8();
  public static final Type META = new Meta();

  /** Independent, undivisible, built-in types. */
  public static sealed abstract class Scalar
    extends Type permits Scalar.Signed, Scalar.Unsigned, Scalar.Floating {
    /** Integers with a bit for signedness. */
    public static sealed abstract class Signed extends
      Scalar permits Signed.I1, Signed.I2, Signed.I4, Signed.I8, Signed.IX {
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
    }

    /** Integers without a sign bit. */
    public static sealed abstract class Unsigned extends
      Scalar permits Unsigned.U1, Unsigned.U2, Unsigned.U4, Unsigned.U8, Unsigned.UX {
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
    }

    /**
     * Real numbers stored as sign, fraction and exponent such that the position
     * of the decimal point can vary.
     */
    public static sealed abstract class Floating
      extends Scalar permits Floating.F4, Floating.F8 {
      /** 4 byte, floating-point real. */
      public static final class F4 extends Floating {
      }

      /** 8 byte, floating-point real. */
      public static final class F8 extends Floating {
      }
    }
  }

  /** Type type. */
  public static final class Meta extends Type {
  }
}
