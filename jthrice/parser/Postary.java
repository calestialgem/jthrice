package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class Postary extends Expression {
  static Postary of(Expression operand, Lexeme operator) {
    return new Postary(Portion.of(operand.portion, operator.portion), operand,
      operator);
  }

  public final Expression operand;
  public final Lexeme     operator;

  private Postary(Portion portion, Expression operand, Lexeme operator) {
    super(portion);
    this.operand  = operand;
    this.operator = operator;
  }
}
