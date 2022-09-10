package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class Nullary extends Expression {
  static Nullary of(Lexeme operator) {
    return new Nullary(operator.portion, operator);
  }

  public final Lexeme operator;

  private Nullary(Portion portion, Lexeme operator) {
    super(portion);
    this.operator = operator;
  }
}
