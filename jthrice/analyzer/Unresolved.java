// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.*;
import jthrice.parser.*;

final class Unresolved {
  static Unresolved of(Identifier identifier, Expression type,
    Expression value) {
    return new Unresolved(identifier.toString(), identifier, type, value);
  }

  public final String     name;
  public final Identifier definition;
  public final Expression type;
  public final Expression value;

  private Unresolved(String name, Identifier definition,
    Expression type, Expression value) {
    this.name       = name;
    this.definition = definition;
    this.type       = type;
    this.value      = value;
  }
}
