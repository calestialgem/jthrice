package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class NullaryExpression extends Expression {
  static NullaryExpression of(NullaryOperator type, Lexeme operator) {
    return new NullaryExpression(operator.portion, type, operator);
  }

  public final NullaryOperator type;
  public final Lexeme          operator;

  private NullaryExpression(Portion portion, NullaryOperator type,
    Lexeme operator) {
    super(portion);
    this.type     = type;
    this.operator = operator;
  }
}
