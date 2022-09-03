// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.io.PrintStream;
import java.util.Objects;

import jthrice.utility.Bug;

/** Portion of a string. */
public final class Portion {
  /** Portion of the line the given location is in. */
  public static Portion ofLine(Location location) {
    return new Portion(location.start(), location.end());
  }

  /** Portion of the begining of the line ending at the given location. */
  public static Portion ofLineStart(Location location) {
    return new Portion(location.start(), location);
  }

  /** Portion of the rest of the line starting from the given location. */
  public static Portion ofLineEnd(Location location) {
    return new Portion(location, location.end());
  }

  /** Location of the first character. */
  public final Location first;
  /** Location of the last character. */
  public final Location last;

  public Portion(Location first, Location last) {
    Bug.check(first.source.equals(last.source),
      "The first and last locations are not from the same source!");
    Bug.check(first.index <= last.index,
      "The first location comes after the last location!");
    this.first = first;
    this.last  = last;
  }

  /** Whether the portion spans multiple lines. */
  public boolean multiline() {
    return this.first.line < this.last.line;
  }

  /**
   * Print the line or lines the portion is in with the portion underlined to
   * the given output stream.
   */
  public void underline(PrintStream out) {
    if (!this.multiline()) {
      this.underlineSingle(out, false);
    } else {
      Portion.ofLineEnd(this.first).underlineSingle(out, true);
      Portion.ofLineStart(this.last).underlineSingle(out, false);
    }
    out.println();
  }

  /**
   * Print the line the portion is in with the portion underlined to the given
   * output stream. Prints `...` if the given continues flag is true.
   */
  private void underlineSingle(PrintStream out, boolean continues) {
    var line = Portion.ofLine(this.first);
    out.printf("%8d | %s%n", line.first.line, line);
    out.printf("%11s", continues ? "... |" : "");
    for (var i = 1; i <= this.last.column; i++) {
      out.printf("%c", i < this.first.column ? ' ' : '~');
    }
    out.println();
  }

  @Override
  public String toString() {
    return this.first.source.sub(this.first.index, this.last.index);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.first, this.last);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Portion other)) {
      return false;
    }
    return Objects.equals(this.first, other.first)
      && Objects.equals(this.last, other.last);
  }
}
