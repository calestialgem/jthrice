// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

/** Exception that shows that there is a bug in the code. */
public class Bug extends RuntimeException {
    /** Throw a `Bug` with the given message. */
    public static void unreachable(String message) throws Bug {
        throw new Bug(message);
    }

    /** If the condition does not hold, throw a `Bug` with the given message. */
    public static void check(boolean condition, String message) throws Bug {
        if (!condition) {
            Bug.unreachable(message);
        }
    }

    public Bug(String message) {
        super(message);
    }
}
