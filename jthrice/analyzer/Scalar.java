// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.math.*;

public sealed abstract class Scalar extends
  Type permits Scalar.Signed, Scalar.Unsigned, Scalar.Floating, Scalar.Rinf {
  public static final I1   I1   = new I1();
  public static final I2   I2   = new I2();
  public static final I4   I4   = new I4();
  public static final I8   I8   = new I8();
  public static final Ix   IX   = new Ix();
  public static final U1   U1   = new U1();
  public static final U2   U2   = new U2();
  public static final U4   U4   = new U4();
  public static final U8   U8   = new U8();
  public static final Ux   UX   = new Ux();
  public static final F4   F4   = new F4();
  public static final F8   F8   = new F8();
  public static final Rinf RINF = new Rinf();

  public static sealed abstract class Signed
    extends Scalar permits I1, I2, I4, I8, Ix {
    private Signed() {
    }
  }

  public static final class I1 extends Signed {
    private I1() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.bitLength() < 8;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "i1";
    }
  }

  public static final class I2 extends Signed {
    private I2() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.bitLength() < 16;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "i2";
    }
  }

  public static final class I4 extends Signed {
    private I4() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.bitLength() < 32;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "i4";
    }
  }

  public static final class I8 extends Signed {
    private I8() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.bitLength() < 64;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "i8";
    }
  }

  public static final class Ix extends Signed {
    private Ix() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.bitLength() < 64;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "ix";
    }

  }

  public static sealed abstract class Unsigned
    extends Scalar permits U1, U2, U4, U8, Ux {
    private Unsigned() {
    }
  }

  public static final class U1 extends Unsigned {
    private U1() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.signum() != -1 && whole.bitLength() <= 8;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "u1";
    }
  }

  public static final class U2 extends Unsigned {
    private U2() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.signum() != -1 && whole.bitLength() <= 16;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "u2";
    }
  }

  public static final class U4 extends Unsigned {
    private U4() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.signum() != -1 && whole.bitLength() <= 32;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "u4";
    }
  }

  public static final class U8 extends Unsigned {
    private U8() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.signum() != -1 && whole.bitLength() <= 64;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "u8";
    }
  }

  public static final class Ux extends Unsigned {
    private Ux() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      try {
        var whole = value.toBigIntegerExact();
        return whole.signum() != -1 && whole.bitLength() <= 64;
      } catch (ArithmeticException e) {
        return false;
      }
    }

    @Override
    public String toString() {
      return "ux";
    }
  }

  public static sealed abstract class Floating extends Scalar permits F4, F8 {
    private Floating() {
    }
  }

  public static final class F4 extends Floating {
    private F4() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      return value.toString().equals(Float.toString(value.floatValue()));
    }

    @Override
    public String toString() {
      return "f4";
    }

  }

  public static final class F8 extends Floating {
    private F8() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      return value.toString().equals(Double.toString(value.doubleValue()));
    }

    @Override
    public String toString() {
      return "f8";
    }

  }

  public static final class Rinf extends Scalar {
    private Rinf() {
    }

    @Override
    public boolean holds(BigDecimal value) {
      return true;
    }

    @Override
    public String toString() {
      return "rinf";
    }
  }

  private Scalar() {
  }

  public abstract boolean holds(BigDecimal value);
}
