// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;

import jthrice.exception.Bug;

/** Location of a character in a source. */
public class Location {
    /** Source that the location is in. */
    public final Source source;
    /** Index of the character in the string. */
    public final int index;
    /** Line number. */
    public final int line;
    /** Column number. */
    public final int column;

    /**
     * Initialize with the location of the character in the given source at the
     * given index.
     */
    public Location(Source source, int index) {
        int line = 1;
        int column = 1;
        Bug.check(source.contents.length() > index, "Index is out of the bounds of the source contents!");
        for (int i = 0; i < index; i++) {
            char c = source.contents.charAt(i);
            if (c == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        this.source = source;
        this.index = index;
        this.line = line;
        this.column = column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, index, line, source);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Location)) {
            return false;
        }
        Location other = (Location) obj;
        return column == other.column && index == other.index && line == other.line
                && Objects.equals(source, other.source);
    }
}
