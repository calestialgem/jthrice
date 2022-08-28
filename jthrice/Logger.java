// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jthrice.lexer.Portion;
import jthrice.lexer.Source;

/** Holds the logs from the compiler. */
public class Logger {
    /** Type and severity of a log. */
    public static enum Level {
        ERROR, WARNING, INFO;
    }

    /** Stream to print the logs to. */
    private final PrintStream out;
    /** Source file that emmits the logs. */
    private final Source source;

    public Logger(PrintStream out, Source source) {
        this.out = out;
        this.source = source;
    }

    /**
     * Log the given message from the given author, for the given portion of the
     * source at the given log level.
     */
    public void log(Logger.Level level, Portion portion, String author, String message) {
        Bug.check(source.equals(portion.first.source), "The portion is not from the source!");
        final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss.SSS");
        out.printf("%s [%s] %s:%d:%d:%d:%d: %s: %s%n", FORMATTER.format(LocalDateTime.now()), author, source.path,
                portion.first.line, portion.first.column, portion.last.line, portion.last.column, level, message);
    }

    /**
     * Log an error with the given message from the given author, for the given
     * portion of the source.
     */
    public void error(Portion portion, String author, String message) {
        log(Level.ERROR, portion, author, message);
    }

    /**
     * Log a warning with the given message from the given author, for the given
     * portion of the source.
     */
    public void warning(Portion portion, String author, String message) {
        log(Level.WARNING, portion, author, message);
    }

    /**
     * Log an info with the given message from the given author, for the given
     * portion of the source.
     */
    public void info(Portion portion, String author, String message) {
        log(Level.INFO, portion, author, message);
    }
}
