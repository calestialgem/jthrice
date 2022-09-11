package jthrice.parser;

import jthrice.lexer.*;

public final class CirnaryOperator extends Operator {
  static CirnaryOperator of(Class<? extends Lexeme> left,
    Class<? extends Lexeme> right) {
    return new CirnaryOperator(left, right);
  }

  private final Class<? extends Lexeme> left;
  private final Class<? extends Lexeme> right;

  private CirnaryOperator(Class<? extends Lexeme> left,
    Class<? extends Lexeme> right) {
    this.left  = left;
    this.right = right;
  }

  boolean left(Lexeme lexeme) {
    return left.isInstance(lexeme);
  }

  boolean right(Lexeme lexeme) {
    return right.isInstance(lexeme);
  }

  String right() {
    return Lexeme.toString(right);
  }

  @Override
  public String toString() {
    return "%s operand %s".formatted(Lexeme.toString(left),
      Lexeme.toString(right));
  }
}
