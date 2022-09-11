package jthrice.parser;

import jthrice.lexer.*;

public final class BinaryOperator extends Operator {
  static BinaryOperator of(Class<? extends Lexeme> operator) {
    return new BinaryOperator(operator);
  }

  private final Class<? extends Lexeme> operator;

  private BinaryOperator(Class<? extends Lexeme> operator) {
    this.operator = operator;
  }

  boolean operator(Lexeme lexeme) {
    return operator.isInstance(lexeme);
  }

  @Override
  public String toString() {
    return "operand %s operand".formatted(Lexeme.toString(operator),
      Lexeme.toString(operator));
  }
}
