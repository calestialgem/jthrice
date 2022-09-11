// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.*;
import jthrice.parser.*;

public final class Variable extends Symbol {
  static Variable of(Identifier identifier, Evaluation evaluation,
    Expression bound) {
    return new Variable(identifier.toString(), identifier, evaluation, bound);
  }

  public final Expression bound;

  private Variable(String name, Identifier declaration, Evaluation evaluation,
    Expression bound) {
    super(name, declaration, evaluation);
    this.bound = bound;
  }
}
