// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.logger;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import jthrice.exception.Bug;
import jthrice.lexer.Portion;
import jthrice.lexer.Source;

/** An account of an event happened in the compiler. */
public class Log {
    /** Type and severity of a log. */
    public static enum Level {
        ERROR, WARNING, INFO, DEBUG;
    }

    /** Log with the given contents and current time. */
    public static Log of(Source source, Portion portion, String message, Level level, String author) {
        return new Log(source, portion, message, level, author, LocalDateTime.now());
    }

    /** Source file that emmited the log. */
    public final Source source;
    /** Portion of the source that emmited the log. */
    public final Portion portion;
    /** Explanation of the log. */
    public final String message;
    /** Level. */
    public final Level level;
    /** Part of the compiler that emmited the log. */
    public final String author;
    /** Date and time when the log was emmited. */
    public final LocalDateTime dateTime;

    public Log(Source source, Portion portion, String message, Level level, String author, LocalDateTime dateTime) {
        Bug.check(source.equals(portion.first.source), "The portion is not from the source!");
        this.source = source;
        this.portion = portion;
        this.message = message;
        this.level = level;
        this.author = author;
        this.dateTime = dateTime;
    }

    /** Print the log to the given stream. */
    public void print(PrintStream stream) {
        final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss.SSS");
        stream.printf("%s [%s] %s:%d:%d:%d:%d: %s: %s%n", FORMATTER.format(dateTime), author, source.path,
                portion.first.line, portion.first.column, portion.last.line, portion.last.column, level, message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, dateTime, level, message, portion, source);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Log)) {
            return false;
        }
        Log other = (Log) obj;
        return Objects.equals(author, other.author) && Objects.equals(dateTime, other.dateTime) && level == other.level
                && Objects.equals(message, other.message) && Objects.equals(portion, other.portion)
                && Objects.equals(source, other.source);
    }
}
