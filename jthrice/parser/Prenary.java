package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class Prenary extends Expression {
  static Prenary of(Lexeme operator, Expression operand) {
    return new Prenary(Portion.of(operator.portion, operand.portion), operator,
      operand);
  }

  public final Lexeme     operator;
  public final Expression operand;

  private Prenary(Portion portion, Lexeme operator, Expression operand) {
    super(portion);
    this.operator = operator;
    this.operand  = operand;
  }
}
