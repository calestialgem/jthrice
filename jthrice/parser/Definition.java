package jthrice.parser;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class Definition extends Statement {
  static Definition of(Identifier name, Expression type, Expression value) {
    return new Definition(Portion.of(name.portion, value.portion), name, type,
      value);
  }

  public final Identifier name;
  public final Expression type;
  public final Expression value;

  private Definition(Portion portion, Identifier name, Expression type,
    Expression value) {
    super(portion);
    this.name  = name;
    this.type  = type;
    this.value = value;
  }
}
