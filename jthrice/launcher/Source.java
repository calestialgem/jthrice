// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import jthrice.Bug;

/** A UTF-8 source file. */
public class Source {
    /** Character that represents the end of file. */
    public static final char EOF = 0;
    /** File extension of Thrice source files. */
    public static final String EXTENSION = "tr";

    /** Name of the source file without the file extensions. */
    public final String name;
    /** Path to the source file. */
    public final Path path;
    /** Contents of the source file. */
    public final String contents;

    public Source(String name) throws IOException {
        this.name = name;
        this.path = Path.of(name + '.' + EXTENSION).toAbsolutePath();
        contents = Files.readString(path).replaceAll("\r", "") + EOF + '\n';
    }

    /** Amount of characters. */
    public int size() {
        return contents.length();
    }

    /** Whether there is a character at the given index. */
    public boolean exists(int index) {
        return index >= 0 && index < size();
    }

    /** Character at the given index. */
    public char at(int index) {
        Bug.check(exists(index), "Index out of contents!");
        return contents.charAt(index);
    }

    /** Characters from the given first to the last index. */
    public String sub(int first, int last) {
        Bug.check(exists(first), "First index out of contents!");
        Bug.check(exists(last), "Last index out of contents!");
        return contents.substring(first, last + 1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, path);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Source)) {
            return false;
        }
        Source other = (Source) obj;
        return Objects.equals(contents, other.contents) && Objects.equals(path, other.path);
    }
}
