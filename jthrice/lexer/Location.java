// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import jthrice.exception.Bug;

/** Location of a character in a string. */
public class Location {
    /** String the location is in. */
    public final String string;
    /** Index of the character in the string. */
    public final int index;
    /** Line number. */
    public final int line;
    /** Column number. */
    public final int column;

    /**
     * Initialize with the location of the character in the given string at the
     * given index.
     */
    public Location(String string, int index) {
        int line = 1;
        int column = 1;
        Bug.check(string.length() > index, "Index is out of the bounds of the string!");
        for (int i = 0; i < index; i++) {
            char c = string.charAt(i);
            if (c == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        this.string = string;
        this.index = index;
        this.line = line;
        this.column = column;
    }
}
