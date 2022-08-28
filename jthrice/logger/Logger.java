// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.logger;

import java.io.PrintStream;

import jthrice.lexer.Portion;
import jthrice.lexer.Source;

/** Holds the logs from the compiler. */
public class Logger {
    /** Stream to print the logs to. */
    private PrintStream out;

    public Logger(PrintStream out) {
        this.out = out;
    }

    /** Register a log. */
    public void log(Source source, Portion portion, String message, Log.Level level, String author) {
        Log.of(source, portion, message, level, author).print(out);
    }
}
