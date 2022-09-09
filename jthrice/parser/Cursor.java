// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.*;

import jthrice.lexer.*;

final class Cursor {
  static Cursor of(List<Lexeme> lexemes) {
    return new Cursor(lexemes, 0);
  }

  private final List<Lexeme> lexemes;
  private int                next;

  public Cursor(List<Lexeme> lexemes, int next) {
    this.lexemes = lexemes;
    this.next    = next;
  }

  Lexeme current() {
    return lexemes.get(next - 1);
  }

  boolean has() {
    return next < lexemes.size();
  }

  Lexeme next() {
    return lexemes.get(next);
  }

  Cursor consume() {
    next++;
    return this;
  }

  @SuppressWarnings("unchecked")
  <T extends Lexeme> T skipUntil(Class<? extends T> type) {
    while (consume().has()) {
      if (type.isInstance(next())) {
        var lexeme = next();
        consume();
        return (T) lexeme;
      }
    }
    return null;
  }
}
