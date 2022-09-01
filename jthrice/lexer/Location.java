// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;
import java.util.Optional;

import jthrice.Bug;
import jthrice.launcher.Source;

/** Location of a character in a source. */
public class Location {
    /** Location from the given source at the given index. */
    public static Optional<Location> of(Source source, int index) {
        if (source.exists(index) && source.at(index) != '\n') {
            return Optional.of(new Location(source, index));
        }
        return Optional.empty();
    }

    /** Location from the given source at the given line and column. */
    public static Optional<Location> of(Source source, int line, int column) {
        if (line >= 1 && column >= 1) {
            var index = 0;
            for (; index < source.size(); index++) {
                if (source.at(index) == '\n') {
                    line--;
                    continue;
                }
                if (line == 1) {
                    column--;
                    if (column == 0) {
                        return Optional.of(new Location(source, index, line, column));
                    }
                }
            }
        }
        return Optional.empty();
    }

    /** First location in the given source starting at the given index. */
    public static Optional<Location> ofFirst(Source source, int start) {
        if (start >= 0) {
            for (int i = start; i < source.size(); i++) {
                if (source.at(i) != '\n') {
                    return Optional.of(new Location(source, i));
                }
            }
        }
        return Optional.empty();
    }

    /** Last location in the given source ending at the given index. */
    public static Optional<Location> ofLast(Source source, int end) {
        if (end < source.size()) {
            for (int i = end; i >= 0; i--) {
                if (source.at(i) != '\n') {
                    return Optional.of(new Location(source, i));
                }
            }
        }
        return Optional.empty();
    }

    /** First location in the source. */
    public static Optional<Location> ofFirst(Source source) {
        return ofFirst(source, 0);
    }

    /** Last location in the source. */
    public static Optional<Location> ofLast(Source source) {
        return ofLast(source, source.size() - 1);
    }

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

    /** Character at the location. */
    public char get() {
        return source.at(index);
    }

    /** Location after this one. */
    public Optional<Location> next() {
        return ofFirst(source, index + 1);
    }

    /** Location before this one. */
    public Optional<Location> previous() {
        return ofLast(source, index - 1);
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
