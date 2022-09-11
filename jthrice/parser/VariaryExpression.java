package jthrice.parser;

import java.util.*;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class VariaryExpression extends Expression {
  static VariaryExpression of(VariaryOperator type, Expression first,
    Lexeme left,
    List<Expression> remaining,
    List<Lexeme> between, Lexeme right) {
    return new VariaryExpression(Portion.of(first.portion, right.portion), type,
      first, left,
      remaining, between, right);
  }

  public final VariaryOperator  type;
  public final Expression       first;
  public final Lexeme           left;
  public final List<Expression> remaining;
  public final List<Lexeme>     between;
  public final Lexeme           right;

  private VariaryExpression(Portion portion, VariaryOperator type,
    Expression first, Lexeme left,
    List<Expression> remaining, List<Lexeme> between, Lexeme right) {
    super(portion);
    this.type      = type;
    this.first     = first;
    this.left      = left;
    this.remaining = remaining;
    this.between   = between;
    this.right     = right;
  }
}
