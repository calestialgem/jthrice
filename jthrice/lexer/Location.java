// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;

import jthrice.launcher.Source;
import jthrice.utility.Bug;

/** Location of a character in a source. */
public final class Location {
  /** Source that the location is in. */
  public final Source source;
  /** Index of the character in the string. */
  public final int    index;
  /** Line number. */
  public final int    line;
  /** Column number. */
  public final int    column;

  public Location(Source source, int index, int line, int column) {
    Bug.check(source.exists(index), "Unexisting location!");
    Bug.check(source.at(index) != '\n', "Invalid source location!");
    Bug.check(line >= 1, "Invalid line number!");
    Bug.check(column >= 1, "Invalid column number!");
    this.source = source;
    this.index  = index;
    this.line   = line;
    this.column = column;
  }

  public Location(Source source, int index) {
    Bug.check(source.exists(index), "Unexisting location!");
    Bug.check(source.at(index) != '\n', "Invalid source location!");
    this.source = source;
    this.index  = index;
    var accumulatedLine   = 1;
    var accumulatedColumn = 1;
    for (var i = 0; i < index; i++) {
      if (source.at(i) == '\n') {
        accumulatedLine++;
        accumulatedColumn = 1;
      } else {
        accumulatedColumn++;
      }
    }
    this.line   = accumulatedLine;
    this.column = accumulatedColumn;
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
        return new Location(this.source, this.index);
      }
    }
    Bug.unreachable("Source file does not end with an empty line!");
    return null;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.column, this.index, this.line, this.source);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Location other)) {
      return false;
    }
    return this.column == other.column && this.index == other.index
      && this.line == other.line && Objects.equals(this.source, other.source);
  }
}
