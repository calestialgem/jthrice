// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import jthrice.launcher.*;

final class Chunk {
  static Chunk of(Source source, int index) {
    return new Chunk(source, index, index);
  }

  private final Source source;
  private final int    first;
  private int          last;

  private Chunk(Source source, int first, int last) {
    this.source = source;
    this.first  = first;
    this.last   = last;
  }

  boolean has() {
    return this.source.exists(this.last);
  }

  char next() {
    return this.source.at(this.last);
  }

  Chunk consume() {
    this.last++;
    return this;
  }

  Portion get() {
    return Portion.of(this.source, this.first, this.last - 1);
  }
}
