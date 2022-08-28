// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.exception;

/** Exception that shows that there is a bug in the code. */
public class Bug extends RuntimeException {
    /** If the condition does not hold, throw a `Bug` with the given message. */
    public static void check(boolean condition, String message) throws Bug {
        if (!condition) {
            throw new Bug(message);
        }
    }

    public Bug(String message) {
        super(message);
    }

    public Bug(String message, Throwable cause) {
        super(message, cause);
    }
}
