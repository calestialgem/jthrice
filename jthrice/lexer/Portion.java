package jthrice.lexer;

/** Portion of a string. */
public class Portion {
    /** Location of the first character. */
    public final Location first;
    /** Location of the last character. */
    public final Location last;

    /** Initialize with the given first and last locations. */
    public Portion(Location first, Location last) {
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
