// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

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

  private final Source source;
  private final int    index;
  private final int    line;
  private final int    column;

  private Location(Source source, int index, int line, int column) {
    this.source = source;
    this.index  = index;
    this.line   = line;
    this.column = column;
  }

  public Location start() {
    return new Location(source, index - column + 1, line, 1);
  }

  public Location end() {
    for (var i = index; source.exists(i); i++) {
      if (source.at(i) == '\n') {
        return new Location(source, i - 1, line, column - index + i - 1);
      }
    }
    return null;
  }

  public boolean local(Location other) {
    return source == other.source;
  }

  public boolean inline(Location other) {
    return line == other.line;
  }

  public int distance(Location other) {
    return other.index - index;
  }

  public Source source() {
    return source;
  }

  public int index() {
    return index;
  }

  public int line() {
    return line;
  }

  public int column() {
    return column;
  }

  public String sub(Location end) {
    return source.sub(index, end.index);
  }
}
