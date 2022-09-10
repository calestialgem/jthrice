package jthrice.parser;

import java.util.*;

import jthrice.launcher.*;
import jthrice.lexer.*;

public final class Root extends Node {
  static Root of(List<Statement> statements, EOF eof) {
    if (statements.size() == 0) {
      return new Root(eof.portion, statements);
    }
    return new Root(Portion.of(statements.get(0).portion,
      statements.get(statements.size() - 1).portion), statements);
  }

  public final List<Statement> statements;

  private Root(Portion portion, List<Statement> statements) {
    super(portion);
    this.statements = statements;
  }
}
