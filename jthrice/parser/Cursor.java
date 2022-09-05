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
  /** Current index. */
  private int                current;

  /** Constructor. */
  public Cursor(List<Lexeme> lexemes, int current) {
    this.lexemes = lexemes;
    this.current = current;
  }

  /** Whether there is a lexeme. */
  boolean has() {
    return this.current < this.lexemes.size();
  }

  /** Current lexeme. */
  Lexeme get() {
    return this.lexemes.get(this.current);
  }

  /** Skip the current lexeme. Returns this. */
  Cursor consume() {
    this.current++;
    return this;
  }

  /** Skip the current lexeme and return whether there is more. */
  boolean next() {
    return this.consume().has();
  }
}
