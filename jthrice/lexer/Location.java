package jthrice.lexer;

/** Location of a character in a string. */
public class Location {
    /** Line number. */
    public final int line;
    /** Column number. */
    public final int column;

    /** Initialize with the given line and column numbers. */
    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }

    /**
     * Initialize with the location of the character in the given string at the
     * given index.
     */
    public Location(String string, int index) {
        int line = 1;
        int column = 1;
        assert (string.length() > index);
        for (int i = 0; i < index; i++) {
            char c = string.charAt(i);
            if (c == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        this.line = line;
        this.column = column;
    }
}
