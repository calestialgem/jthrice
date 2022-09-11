package jthrice.parser;

import jthrice.lexer.*;

public final class PrenaryOperator extends Operator {
  static PrenaryOperator of(Class<? extends Lexeme> operator) {
    return new PrenaryOperator(operator);
  }

  private final Class<? extends Lexeme> operator;

  private PrenaryOperator(Class<? extends Lexeme> operator) {
    this.operator = operator;
  }

  boolean operator(Lexeme lexeme) {
    return operator.isInstance(lexeme);
  }

  @Override
  public String toString() {
    return "%s operand".formatted(Lexeme.toString(operator));
  }
}
