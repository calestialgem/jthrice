// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;

import jthrice.exception.Bug;

/** Portion of a string. */
public class Portion {
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

    /**
     * Initialize with the location of the first and last characters in the given
     * source at the given indicies.
     */
    public Portion(Source source, int first, int last) {
        this(new Location(source, first), new Location(source, last));
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
