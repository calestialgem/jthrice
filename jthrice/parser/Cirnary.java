package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class Cirnary extends Expression {
  static Cirnary of(Lexeme left, Expression operand, Lexeme right) {
    return new Cirnary(Portion.of(left.portion, right.portion), left, operand,
      right);
  }

  public final Lexeme     left;
  public final Expression operand;
  public final Lexeme     right;

  private Cirnary(Portion portion, Lexeme left, Expression operand,
    Lexeme right) {
    super(portion);
    this.left    = left;
    this.operand = operand;
    this.right   = right;
  }
}
