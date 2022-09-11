package jthrice.parser;

import jthrice.lexer.*;

public final class VariaryOperator extends Operator {
  static VariaryOperator of(Class<? extends Lexeme> left,
    Class<? extends Lexeme> between, Class<? extends Lexeme> right) {
    return new VariaryOperator(left, between, right);
  }

  private final Class<? extends Lexeme> left;
  private final Class<? extends Lexeme> between;
  private final Class<? extends Lexeme> right;

  private VariaryOperator(Class<? extends Lexeme> left,
    Class<? extends Lexeme> between,
    Class<? extends Lexeme> right) {
    this.left    = left;
    this.between = between;
    this.right   = right;
  }

  boolean left(Lexeme lexeme) {
    return left.isInstance(lexeme);
  }

  boolean between(Lexeme lexeme) {
    return between.isInstance(lexeme);
  }

  boolean right(Lexeme lexeme) {
    return right.isInstance(lexeme);
  }

  String between() {
    return Lexeme.toString(between);
  }

  String right() {
    return Lexeme.toString(right);
  }

  @Override
  public String toString() {
    return "operand%soperands%s ...%s operand%s".formatted(
      Lexeme.toString(left), Lexeme.toString(between), Lexeme.toString(between),
      Lexeme.toString(right));
  }
}
