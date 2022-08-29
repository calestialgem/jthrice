// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

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
        contents = Files.readString(path).replaceAll("\r", "") + EOF;
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
