package jthrice.parser;

import jthrice.lexer.*;

public final class Prefix extends Operator {
  static Prefix of(Class<? extends Lexeme> operator) {
    return new Prefix(operator);
  }

  private final Class<? extends Lexeme> operator;

  private Prefix(Class<? extends Lexeme> operator) {
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
