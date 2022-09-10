package jthrice.parser;

import jthrice.lexer.*;

public final class Postfix extends Operator {
  static Postfix of(Class<? extends Lexeme> operator) {
    return new Postfix(operator);
  }

  private final Class<? extends Lexeme> operator;

  private Postfix(Class<? extends Lexeme> operator) {
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
