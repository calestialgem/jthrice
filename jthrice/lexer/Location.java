// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;

import jthrice.launcher.Source;
import jthrice.utility.Bug;

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

    public Location(Source source, int index, int line, int column) {
        Bug.check(source.exists(index), "Unexisting location!");
        Bug.check(source.at(index) != '\n', "Invalid source location!");
        Bug.check(line >= 1, "Invalid line number!");
        Bug.check(column >= 1, "Invalid column number!");
        this.source = source;
        this.index = index;
        this.line = line;
        this.column = column;
    }

    public Location(Source source, int index) {
        Bug.check(source.exists(index), "Unexisting location!");
        Bug.check(source.at(index) != '\n', "Invalid source location!");
        this.source = source;
        this.index = index;
        var line = 1;
        var column = 1;
        for (var i = 0; i < index; i++) {
            if (source.at(i) == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        this.line = line;
        this.column = column;
    }

    /** Start of the line this location is in. */
    public Location start() {
        return new Location(source, index - column + 1, line, 1);
    }

    /** End of the line this location is in. */
    public Location end() {
        for (var i = index + 1; i < source.size(); i++) {
            if (source.at(index) != '\n') {
                return new Location(source, index);
            }
        }
        Bug.unreachable("Source file does not end with an empty line!");
        return null;
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
