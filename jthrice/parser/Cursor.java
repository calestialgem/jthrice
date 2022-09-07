// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.*;

import jthrice.lexer.*;

/** Iterator over a lexeme list with state. */
class Cursor {
  /** Cursor at the begining of the given lexemes. */
  static Cursor of(List<Lexeme> lexemes) {
    return new Cursor(lexemes, 0);
  }

  /** Lexemes. */
  private final List<Lexeme> lexemes;
  /** Index of the next lexeme. */
  private int                next;

  /** Constructor. */
  public Cursor(List<Lexeme> lexemes, int next) {
    this.lexemes = lexemes;
    this.next    = next;
  }

  /** Current lexeme. */
  Lexeme current() {
    return this.lexemes.get(this.next - 1);
  }

  /** Whether there is a lexeme left in the list. */
  boolean has() {
    return this.next < this.lexemes.size();
  }

  /** Next lexeme. */
  Lexeme next() {
    return this.lexemes.get(this.next);
  }

  /** Take the next lexeme. Returns this. */
  Cursor consume() {
    this.next++;
    return this;
  }
}
