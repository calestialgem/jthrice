// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

/** Container that can have a value or an error. */
public sealed abstract class Result<T, E> permits Coup<T, E>, Dud<T, E> {
  /** Whether there is a value. */
  public abstract boolean is();

  /** Whether there is not a value. */
  public boolean not() {
    return !is();
  }

  /** Value. */
  public abstract T get();

  /** Error. */
  public abstract E error();
}
