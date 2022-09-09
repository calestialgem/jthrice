// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.io.*;

import jthrice.launcher.*;

public final class Portion {
  public static Portion of(Portion first, Portion last) {
    return new Portion(first.first, last.last);
  }

  public static Portion ofLine(Location location) {
    return new Portion(location.start(), location.end());
  }

  public static Portion ofLineStart(Location location) {
    return new Portion(location.start(), location);
  }

  public static Portion ofLineEnd(Location location) {
    return new Portion(location, location.end());
  }

  public static Portion of(Location first, Location last) {
    if (first.source != last.source || first.index > last.index) {
      return null;
    }
    return new Portion(first, last);
  }

  public static Portion of(Source source, int first, int last) {
    if (first > last) {
      return null;
    }
    return new Portion(Location.of(source, first), Location.of(source, last));
  }

  public final Location first;
  public final Location last;

  private Portion(Location first, Location last) {
    this.first = first;
    this.last  = last;
  }

  public boolean multiline() {
    return first.line < last.line;
  }

  public void underline(PrintStream out) {
    if (!multiline()) {
      underlineSingle(out, false);
    } else {
      Portion.ofLineEnd(first).underlineSingle(out, true);
      Portion.ofLineStart(last).underlineSingle(out, false);
    }
    out.println();
  }

  private void underlineSingle(PrintStream out, boolean continues) {
    var line = Portion.ofLine(first);
    out.printf("%8d | %s%n", line.first.line, line);
    out.printf("%10s", continues ? "... |" : "");
    for (var i = 0; i <= last.column; i++) {
      out.printf("%c", i < first.column ? ' ' : '~');
    }
    out.println();
  }

  @Override
  public String toString() {
    return first.source.sub(first.index, last.index);
  }
}
