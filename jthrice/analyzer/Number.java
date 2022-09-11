package jthrice.analyzer;

import java.util.*;

final class Number {
  static Number of(String number, Base base) {
    number = number.toLowerCase(Locale.ROOT);
    var    dot      = number.indexOf('.');
    var    e        = number.indexOf(base.exponent);
    String whole    = "";
    String fraction = "";
    String exponent = "";

    if (dot == -1) {
      if (e == -1) {
        whole = number;
      } else {
        whole    = number.substring(0, e);
        exponent = number.substring(e + 1);
      }
    } else {
      if (e == -1) {
        whole    = number.substring(0, dot);
        fraction = number.substring(dot + 1);
      } else {
        whole    = number.substring(0, dot);
        fraction = number.substring(dot + 1, e);
        exponent = number.substring(e + 1);
      }
    }

    return new Number(Integer.of(whole, base),
      Integer.of(fraction, base),
      Integer.of(exponent, Base.DECIMAL));
  }

  private final Integer whole;
  private final Integer fraction;
  private final Integer exponent;

  private Number(Integer whole, Integer fraction, Integer exponent) {
    this.whole    = whole;
    this.fraction = fraction;
    this.exponent = exponent;
  }

  public String toString(Base base) {
    return whole.toString(base) + '.' + fraction.toString(base) + base.exponent
      + exponent.toString(Base.DECIMAL);
  }

  @Override
  public String toString() {
    return toString(Base.DECIMAL);
  }
}
