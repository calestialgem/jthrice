// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import jthrice.Bug;
import jthrice.lexer.Portion;

/** A UTF-8 source file. */
public class Source {
    /** Path to the source file. */
    public final String path;
    /** Contents of the source file. */
    public final String contents;
    /** Amount of errors that originated in the source file. */
    private int errors;
    /** Amount of warnings that originated in the source file. */
    private int warnings;

    public Source(Path path) throws IOException {
        this.path = path.toAbsolutePath().toString();
        contents = Files.readString(path).replaceAll("\r", "");
    }

    /** Log the given message from the given author at the given log level. */
    private void log(String author, String severity, String message) {
        System.out.printf("[%s] %s: %s: %s%n", author, path, severity, message);
    }

    /** Log an error with the given message from the given author. */
    public void error(String author, String message) {
        log(author, "error", message);
        errors++;
    }

    /** Log a warning with the given message from the given author. */
    public void warning(String author, String message) {
        log(author, "warning", message);
        warnings++;
    }

    /** Log an info with the given message from the given author. */
    public void info(String author, String message) {
        log(author, "info", message);
    }

    /**
     * Log the given message from the given author, for the given portion of the
     * source at the given log level.
     */
    public void log(String author, Portion portion, String severity, String message) {
        Bug.check(equals(portion.first.source), "The portion is not from the source!");
        System.out.printf("[%s] %s:%d:%d:%d:%d: %s: %s%n", author, path, portion.first.line, portion.first.column,
                portion.last.line, portion.last.column, severity, message);
        portion.underline(System.out);
    }

    /**
     * Log an error with the given message from the given author, for the given
     * portion of the source.
     */
    public void error(String author, Portion portion, String message) {
        log(author, portion, "error", message);
        errors++;
    }

    /**
     * Log a warning with the given message from the given author, for the given
     * portion of the source.
     */
    public void warning(String author, Portion portion, String message) {
        log(author, portion, "warning", message);
        warnings++;
    }

    /**
     * Log an info with the given message from the given author, for the given
     * portion of the source.
     */
    public void info(String author, Portion portion, String message) {
        log(author, portion, "info", message);
    }

    /** Amount of errors that originated in the source file. */
    public int errors() {
        return errors;
    }

    /** Amount of warnings that originated in the source file. */
    public int warnings() {
        return warnings;
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
