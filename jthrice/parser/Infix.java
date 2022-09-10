package jthrice.parser;

import jthrice.lexer.*;

final class Infix extends Operator {
  static Infix of(Class<? extends Lexeme> operator) {
    return new Infix(operator);
  }

  private final Class<? extends Lexeme> operator;

  private Infix(Class<? extends Lexeme> operator) {
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
