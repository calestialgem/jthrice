package jthrice.parser;

import jthrice.lexer.*;

final class Nofix extends Operator {
  static Nofix of(Class<? extends Lexeme> operator) {
    return new Nofix(operator);
  }

  private final Class<? extends Lexeme> operator;

  private Nofix(Class<? extends Lexeme> operator) {
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
