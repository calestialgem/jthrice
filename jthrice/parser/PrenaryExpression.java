package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class PrenaryExpression extends Expression {
  static PrenaryExpression of(PrenaryOperator type, Lexeme operator,
    Expression operand) {
    return new PrenaryExpression(Portion.of(operator.portion, operand.portion),
      type,
      operator,
      operand);
  }

  public final PrenaryOperator type;
  public final Lexeme          operator;
  public final Expression      operand;

  private PrenaryExpression(Portion portion, PrenaryOperator type,
    Lexeme operator,
    Expression operand) {
    super(portion);
    this.type     = type;
    this.operator = operator;
    this.operand  = operand;
  }
}
