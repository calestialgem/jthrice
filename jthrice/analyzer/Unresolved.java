// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.*;
import jthrice.parser.*;

final class Unresolved {
  static Unresolved of(Lexeme.Identifier identifier, Node.Expression type,
    Node.Expression value) {
    return new Unresolved(identifier.value, identifier, type, value);
  }

  public final String            name;
  public final Lexeme.Identifier definition;
  public final Node.Expression   type;
  public final Node.Expression   value;

  private Unresolved(String name, Lexeme.Identifier definition,
    Node.Expression type, Node.Expression value) {
    this.name       = name;
    this.definition = definition;
    this.type       = type;
    this.value      = value;
  }
}
