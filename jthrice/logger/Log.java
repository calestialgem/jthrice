package jthrice.logger;

import java.time.LocalDateTime;

import jthrice.lexer.Portion;

/** An account of an event happened in the compiler. */
public class Log {
    /** Type and severity of a log. */
    public static enum Level {
        ERROR, WARNING, INFO, DEBUG;
    }

    /** Path to the source file that emmited the log. */
    public final String file;
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
    Log(String file, Portion portion, String message, Level level, String author) {
        this.file = file;
        this.portion = portion;
        this.message = message;
        this.level = level;
        this.author = author;
        dateTime = LocalDateTime.now();
    }
}
