// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;

import jthrice.Bug;

/** Portion of a string. */
public class Portion {
    /**
     * Portion of the given source from the given first to the last index,
     * inclusive.
     */
    public static Portion of(Source source, int first, int last) {
        return new Portion(new Location(source, first), new Location(source, last));
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
