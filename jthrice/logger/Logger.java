// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.logger;

import java.io.PrintStream;
import java.util.ArrayList;

import jthrice.lexer.Portion;
import jthrice.lexer.Source;

/** Holds the logs from the compiler. */
public class Logger {
    /** Log list. */
    private ArrayList<Log> logs;

    /** Initialize with empty logs. */
    public Logger() {
        logs = new ArrayList<>();
    }

    /** Register a log. */
    public void log(Source source, Portion portion, String message, Log.Level level, String author) {
        logs.add(new Log(source, portion, message, level, author));
    }

    /** Print all the logs to the standard output. */
    public void print() {
        print(System.out);
    }

    /** Print all the logs to the given stream. */
    public void print(PrintStream stream) {
        for (Log log : logs) {
            log.print(stream);
        }
    }
}
