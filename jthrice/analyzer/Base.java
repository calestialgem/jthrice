package jthrice.analyzer;

final class Base {
  static final Base DECIMAL = new Base("0123456789", 'e', 10);

  final String digits;
  final char   exponent;
  final long   exponentVal;

  private Base(String digits, char exponent, long exponentVal) {
    this.digits      = digits;
    this.exponent    = exponent;
    this.exponentVal = exponentVal;
  }

  long radix() {
    return digits.length();
  }

  long digit(char character) {
    return digits.indexOf(character);
  }

  char toDigit(long digit) {
    return digits.charAt((int) digit);
  }
}
