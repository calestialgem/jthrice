// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.*;
import jthrice.parser.*;

/** Unresolved symbol. */
public final class Unresolved {
  /** Unresolved symbol with the given identifier, type and value
   * expressions. */
  public static Unresolved of(Lexeme.Identifier identifier,
    Node.Expression type, Node.Expression value) {
    return new Unresolved(identifier.value, identifier, type, value);
  }

  /** String that matches to the symbol. */
  public final String            name;
  /** Lexeme that defines the symbol. */
  public final Lexeme.Identifier definition;
  /** Expression that decleares the type. */
  public final Node.Expression   type;
  /** Expression that defines the value. */
  public final Node.Expression   value;

  /** Constructor. */
  private Unresolved(String name, Lexeme.Identifier definition,
    Node.Expression type, Node.Expression value) {
    this.name       = name;
    this.definition = definition;
    this.type       = type;
    this.value      = value;
  }
}
