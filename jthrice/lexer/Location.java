// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import jthrice.launcher.*;
import jthrice.utility.*;

/** Location of a character in a source. */
public final class Location {
  /** Location of the character at the given index in the given source. */
  public static Maybe<Location> of(Source source, int index) {
    if (!source.exists(index) || source.at(index) == '\n') {
      return None.of();
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
    return Some.of(new Location(source, index, line, column));
  }

  /** Source that the location is in. */
  public final Source source;
  /** Index of the character in the string. */
  public final int    index;
  /** Line number. */
  public final int    line;
  /** Column number. */
  public final int    column;

  /** Constructor. */
  private Location(Source source, int index, int line, int column) {
    this.source = source;
    this.index  = index;
    this.line   = line;
    this.column = column;
  }

  /** Start of the line this location is in. */
  public Location start() {
    return new Location(this.source, this.index - this.column + 1, this.line,
      1);
  }

  /** End of the line this location is in. */
  public Location end() {
    for (var i = this.index + 1; i < this.source.size(); i++) {
      if (this.source.at(this.index) != '\n') {
        return new Location(this.source, i, this.line,
          this.column - this.index + i);
      }
    }
    Bug.unreachable("Source file does not end with an empty line!");
    return null;
  }
}
