package jthrice.analyzer;

final class Integer {
  static Integer of(String number, Base base) {
    var sign = number.startsWith("-");
    if (sign || number.startsWith("+")) {
      number = number.substring(1);
    }
    long bits = 0;
    for (var c : number.toCharArray()) {
      if (c == '_') {
        continue;
      }
      bits *= base.radix();
      bits += base.digit(c);
    }
    return new Integer(bits, sign);
  }

  final long    bits;
  final boolean sign;

  private Integer(long bits, boolean sign) {
    this.bits = bits;
    this.sign = sign;
  }

  String toString(Base base) {
    var buffer = new StringBuilder();
    if (sign) {
      buffer.append('-');
    }
    var place = 1;
    while (bits / place >= base.radix()) {
      place *= base.radix();
    }
    for (var num = bits; place >= 1; num %= place, place /= base.radix()) {
      buffer.append(base.toDigit(num / place));
    }
    return buffer.toString();
  }

  @Override
  public String toString() {
    return toString(Base.DECIMAL);
  }
}
