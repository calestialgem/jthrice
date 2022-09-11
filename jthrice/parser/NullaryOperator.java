package jthrice.parser;

import jthrice.lexer.*;

public final class NullaryOperator extends Operator {
  static NullaryOperator of(Class<? extends Lexeme> operator) {
    return new NullaryOperator(operator);
  }

  private final Class<? extends Lexeme> operator;

  private NullaryOperator(Class<? extends Lexeme> operator) {
    this.operator = operator;
  }

  boolean operator(Lexeme lexeme) {
    return operator.isInstance(lexeme);
  }

  @Override
  public String toString() {
    return "%s".formatted(Lexeme.toString(operator));
  }
}
