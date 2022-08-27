package jthrice.logger;

import java.util.ArrayList;

import jthrice.lexer.Portion;

/** Holds the logs from the compiler. */
public class Logger {
    /** Log list. */
    private ArrayList<Log> logs;

    /** Initialize with empty logs. */
    public Logger() {
        logs = new ArrayList<>();
    }

    /** Register a log. */
    public void log(String file, Portion portion, String message, Log.Level level, String author) {
        logs.add(new Log(file, portion, message, level, author));
    }
}
