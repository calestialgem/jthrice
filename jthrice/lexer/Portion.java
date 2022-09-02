// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.io.PrintStream;
import java.util.Objects;

import jthrice.launcher.Source;
import jthrice.utility.Bug;

/** Portion of a string. */
public class Portion {
    /**
     * Portion of the given source from the given first to the last index,
     * inclusive.
     */
    public static Portion of(Source source, int first, int last) {
        return new Portion(new Location(source, first), new Location(source, last));
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

    /** Location of the first character. */
    public final Location first;
    /** Location of the last character. */
    public final Location last;

    /** Initialize with the given first and last locations. */
    public Portion(Location first, Location last) {
        Bug.check(first.source.equals(last.source), "The first and last locations are not from the same source!");
        Bug.check(first.index <= last.index, "The first location comes after the last location!");
        this.first = first;
        this.last = last;
    }

    /** Whether the portion spans multiple lines. */
    public boolean multiline() {
        return first.line < last.line;
    }

    /** Print the line or lines the portion is in with the portion underlined. */
    public void underline(PrintStream out) {
        if (!multiline()) {
            underlineSingle(out, false);
        } else {
            ofLineEnd(first).underlineSingle(out, true);
            ofLineStart(last).underlineSingle(out, false);
        }
        out.println();
    }

    /** Print the line the portion is in with the portion underlined. */
    private void underlineSingle(PrintStream out, boolean continues) {
        var line = Portion.ofLine(first);
        out.printf("%8d | %s%n", line.first.line, line);
        out.printf("%11s", continues ? "... |" : "");
        for (var i = 1; i <= last.column; i++) {
            out.printf("%c", i < first.column ? ' ' : '~');
        }
        out.println();
    }

    @Override
    public String toString() {
        return first.source.sub(first.index, last.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, last);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Portion)) {
            return false;
        }
        Portion other = (Portion) obj;
        return Objects.equals(first, other.first) && Objects.equals(last, other.last);
    }
}
