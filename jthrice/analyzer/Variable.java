// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.Lexeme.*;
import jthrice.parser.*;

/** Symbol of a chunk of memory. */
public final class Variable extends Symbol {
  /** Variable of the given identifier, type and bound expression. */
  static Variable of(Identifier identifier, Evaluation evaluation,
    Node.Expression bound) {
    return new Variable(identifier.value, identifier, evaluation, bound);
  }

  /** Expression that is bound to the variable in definition. */
  public final Node.Expression bound;

  /** Constructor. */
  private Variable(String name, Identifier declaration, Evaluation evaluation,
    Node.Expression bound) {
    super(name, declaration, evaluation);
    this.bound = bound;
  }
}
