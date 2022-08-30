// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;
import java.util.Optional;

import jthrice.Bug;
import jthrice.launcher.Source;

/** Location of a character in a source. */
public class Location {
    /** First location in the source. */
    public static Optional<Location> ofFirst(Source source) {
        for (int i = 0; i < source.contents.length(); i++) {
            if (source.contents.charAt(i) != '\n') {
                return Optional.of(new Location(source, i));
            }
        }
        return Optional.empty();
    }

    /** Source that the location is in. */
    public final Source source;
    /** Index of the character in the string. */
    public final int index;
    /** Line number. */
    public final int line;
    /** Column number. */
    public final int column;

    public Location(Source source, int index) {
        Bug.check(source.contents.length() > index, "Index is out of the bounds of the source contents!");
        Bug.check(index >= 0, "Index is negative!");
        Bug.check(source.contents.charAt(index) != '\n', "The character is a new line!");
        this.source = source;
        this.index = index;
        int line = 1;
        int column = 1;
        for (int i = 0; i < index; i++) {
            char c = source.contents.charAt(i);
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

    public Location(Source source, int line, int column) {
        Bug.check(line >= 1, "Line number is not positive!");
        Bug.check(column >= 1, "Column number is not positive!");
        this.source = source;
        this.line = line;
        this.column = column;
        int index = 0;
        for (; index < source.contents.length(); index++) {
            char c = source.contents.charAt(index);
            if (c == '\n') {
                line--;
                continue;
            }
            if (line == 1) {
                column--;
                if (column == 0) {
                    break;
                }
            }
        }
        Bug.check(index < source.contents.length(), "There is no character at the given line and column!");
        Bug.check(source.contents.charAt(index) != '\n', "The character is a new line!");
        this.index = index;
    }

    /** Start of the line this location is in. */
    public Location start() {
        return new Location(source, line, 1);
    }

    /** End of the line this location is in. */
    public Location end() {
        int index = this.index;
        while (index < source.contents.length() && source.contents.charAt(index) != '\n') {
            index++;
        }
        return new Location(source, index - 1);
    }

    /** Character at the location. */
    public char get() {
        return source.contents.charAt(index);
    }

    /** Location after this one. */
    public Optional<Location> next() {
        for (int i = index + 1; i < source.contents.length(); i++) {
            if (source.contents.charAt(i) != '\n') {
                return Optional.of(new Location(source, i));
            }
        }
        return Optional.empty();
    }

    /** Location before this one. */
    public Optional<Location> previous() {
        for (int i = index - 1; i >= 0; i--) {
            if (source.contents.charAt(i) != '\n') {
                return Optional.of(new Location(source, i));
            }
        }
        return Optional.empty();
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
