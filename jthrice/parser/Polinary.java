package jthrice.parser;

import java.util.*;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class Polinary extends Expression {
  static Polinary of(Expression first, Lexeme left, List<Expression> remaining,
    List<Lexeme> between, Lexeme right) {
    return new Polinary(Portion.of(first.portion, right.portion), first, left,
      remaining, between, right);
  }

  public final Expression       first;
  public final Lexeme           left;
  public final List<Expression> remaining;
  public final List<Lexeme>     between;
  public final Lexeme           right;

  private Polinary(Portion portion, Expression first, Lexeme left,
    List<Expression> remaining, List<Lexeme> between, Lexeme right) {
    super(portion);
    this.first     = first;
    this.left      = left;
    this.remaining = remaining;
    this.between   = between;
    this.right     = right;
  }
}
