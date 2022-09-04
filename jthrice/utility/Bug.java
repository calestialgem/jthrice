// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

/** Exception that shows that there is a bug in the code. */
public final class Bug extends RuntimeException {
  private static final long serialVersionUID = -5645741121804615709L;

  /** Throw a `Bug` with the given message. */
  public static void unreachable(String message) throws Bug {
    throw Bug.of(message);
  }

  /** If the condition does not hold, throw a `Bug` with the given message. */
  public static void check(boolean condition, String message) throws Bug {
    if (!condition) {
      Bug.unreachable(message);
    }
  }

  /** Bug with the given message. */
  public static Bug of(String message) {
    return new Bug(message);
  }

  /** Constructor. */
  private Bug(String message) {
    super(message);
  }
}
