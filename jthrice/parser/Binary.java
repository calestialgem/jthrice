package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class Binary extends Expression {
  static Binary of(Expression left, Lexeme operator, Expression right) {
    return new Binary(Portion.of(left.portion, right.portion), left, operator,
      right);
  }

  public final Expression left;
  public final Lexeme     operator;
  public final Expression right;

  private Binary(Portion portion, Expression left, Lexeme operator,
    Expression right) {
    super(portion);
    this.left     = left;
    this.operator = operator;
    this.right    = right;
  }
}
