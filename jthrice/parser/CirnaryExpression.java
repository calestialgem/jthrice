package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class CirnaryExpression extends Expression {
  static CirnaryExpression of(CirnaryOperator type, Lexeme left,
    Expression operand, Lexeme right) {
    return new CirnaryExpression(Portion.of(left.portion, right.portion), type,
      left,
      operand,
      right);
  }

  public final CirnaryOperator type;
  public final Lexeme          left;
  public final Expression      operand;
  public final Lexeme          right;

  private CirnaryExpression(Portion portion, CirnaryOperator type, Lexeme left,
    Expression operand,
    Lexeme right) {
    super(portion);
    this.type    = type;
    this.left    = left;
    this.operand = operand;
    this.right   = right;
  }
}
