package jthrice.parser;

import jthrice.lexer.*;

public final class PostaryOperator extends Operator {
  static PostaryOperator of(Class<? extends Lexeme> operator) {
    return new PostaryOperator(operator);
  }

  private final Class<? extends Lexeme> operator;

  private PostaryOperator(Class<? extends Lexeme> operator) {
    this.operator = operator;
  }

  boolean operator(Lexeme lexeme) {
    return operator.isInstance(lexeme);
  }

  @Override
  public String toString() {
    return "operand %s".formatted(Lexeme.toString(operator));
  }
}
