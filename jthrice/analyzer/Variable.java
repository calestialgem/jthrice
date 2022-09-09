// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.Lexeme.*;
import jthrice.parser.*;

public final class Variable extends Symbol {
  static Variable of(Identifier identifier, Evaluation evaluation,
    Node.Expression bound) {
    return new Variable(identifier.value, identifier, evaluation, bound);
  }

  public final Node.Expression bound;

  private Variable(String name, Identifier declaration, Evaluation evaluation,
    Node.Expression bound) {
    super(name, declaration, evaluation);
    this.bound = bound;
  }
}
