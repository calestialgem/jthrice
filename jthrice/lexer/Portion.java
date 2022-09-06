// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.io.*;

import jthrice.launcher.*;

/** Portion of a string. */
public final class Portion {
  /** Portion from the begining of the given first portion to the end of the
   * given last portion. */
  public static Portion of(Portion first, Portion last) {
    return new Portion(first.first, last.last);
  }

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

  /** Portion from the given start location to the given end location. */
  public static Portion of(Location first, Location last) {
    if (first.source != last.source || first.index > last.index) {
      return null;
    }
    return new Portion(first, last);
  }

  /** Portion from the given start index to the given end index in the given
   * source. */
  public static Portion of(Source source, int first, int last) {
    if (first > last) {
      return null;
    }
    return new Portion(Location.of(source, first), Location.of(source, last));
  }

  /** Location of the first character. */
  public final Location first;
  /** Location of the last character. */
  public final Location last;

  /** Constructor. */
  private Portion(Location first, Location last) {
    this.first = first;
    this.last  = last;
  }

  /** Whether the portion spans multiple lines. */
  public boolean multiline() {
    return this.first.line < this.last.line;
  }

  /** Print the line or lines the portion is in with the portion underlined to
   * the given output stream. */
  public void underline(PrintStream out) {
    if (!this.multiline()) {
      this.underlineSingle(out, false);
    } else {
      Portion.ofLineEnd(this.first).underlineSingle(out, true);
      Portion.ofLineStart(this.last).underlineSingle(out, false);
    }
    out.println();
  }

  /** Print the line the portion is in with the portion underlined to the given
   * output stream. Prints `...` if the given continues flag is true. */
  private void underlineSingle(PrintStream out, boolean continues) {
    var line = Portion.ofLine(this.first);
    out.printf("%8d | %s%n", line.first.line, line);
    out.printf("%10s", continues ? "... |" : "");
    for (var i = 0; i <= this.last.column; i++) {
      out.printf("%c", i < this.first.column ? ' ' : '~');
    }
    out.println();
  }

  @Override
  public String toString() {
    return this.first.source.sub(this.first.index, this.last.index);
  }
}
