package jthrice.lexer;

import jthrice.exception.Bug;

/** Portion of a string. */
public class Portion {
    /** Location of the first character. */
    public final Location first;
    /** Location of the last character. */
    public final Location last;

    /** Initialize with the given first and last locations. */
    public Portion(Location first, Location last) {
        Bug.check(first.string.equals(last.string), "The first and last locations are not from the same string!");
        Bug.check(first.index <= last.index, "The first location comes after the last location!");
        this.first = first;
        this.last = last;
    }

    /**
     * Initialize with the location of the first and last characters in the given
     * string at the given indicies.
     */
    public Portion(String string, int first, int last) {
        this(new Location(string, first), new Location(string, last));
    }
}
