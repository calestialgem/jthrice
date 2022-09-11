package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class PostaryExpression extends Expression {
  static PostaryExpression of(PostaryOperator type, Expression operand,
    Lexeme operator) {
    return new PostaryExpression(Portion.of(operand.portion, operator.portion),
      type,
      operand,
      operator);
  }

  public final PostaryOperator type;
  public final Expression      operand;
  public final Lexeme          operator;

  private PostaryExpression(Portion portion, PostaryOperator type,
    Expression operand,
    Lexeme operator) {
    super(portion);
    this.type     = type;
    this.operand  = operand;
    this.operator = operator;
  }
}
