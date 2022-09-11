package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class BinaryExpression extends Expression {
  static BinaryExpression of(BinaryOperator type, Expression left,
    Lexeme operator,
    Expression right) {
    return new BinaryExpression(Portion.of(left.portion, right.portion), type,
      left,
      operator,
      right);
  }

  public final BinaryOperator type;
  public final Expression     left;
  public final Lexeme         operator;
  public final Expression     right;

  private BinaryExpression(Portion portion, BinaryOperator type,
    Expression left, Lexeme operator,
    Expression right) {
    super(portion);
    this.type     = type;
    this.left     = left;
    this.operator = operator;
    this.right    = right;
  }
}
