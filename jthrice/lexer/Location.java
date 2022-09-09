// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import jthrice.launcher.*;

public final class Location {
  public static Location of(Source source, int index) {
    if (!source.exists(index) || source.at(index) == '\n') {
      return null;
    }
    var line   = 1;
    var column = 1;
    for (var i = 0; i < index; i++) {
      if (source.at(i) == '\n') {
        line++;
        column = 1;
      } else {
        column++;
      }
    }
    return new Location(source, index, line, column);
  }

  public final Source source;
  public final int    index;
  public final int    line;
  public final int    column;

  private Location(Source source, int index, int line, int column) {
    this.source = source;
    this.index  = index;
    this.line   = line;
    this.column = column;
  }

  public Location start() {
    return new Location(this.source, this.index - this.column + 1, this.line,
      1);
  }

  public Location end() {
    for (var i = this.index; this.source.exists(i); i++) {
      if (this.source.at(i) == '\n') {
        return new Location(this.source, i - 1, this.line,
          this.column - this.index + i - 1);
      }
    }
    return null;
  }
}
