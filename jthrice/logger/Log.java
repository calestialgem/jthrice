// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.logger;

import java.io.PrintStream;
import java.time.LocalDateTime;
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

    /** Initialize with the given file path, portion, message, level, and author. */
    Log(Source source, Portion portion, String message, Level level, String author) {
        Bug.check(source.equals(portion.first.source), "The portion is not from the source!");
        this.source = source;
        this.portion = portion;
        this.message = message;
        this.level = level;
        this.author = author;
        dateTime = LocalDateTime.now();
    }

    /** Print the log to the given stream. */
    public void print(PrintStream stream) {
        stream.printf("%1$tY.%1$tm.%1$td.%1$tH.%1$tM.%1$tS [%s] %s:%d:%d:%d:%d: %s: %s", dateTime, author, source.path,
                portion.first.line, portion.first.column, portion.last.line, portion.last.column, level.toString(),
                message);
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
