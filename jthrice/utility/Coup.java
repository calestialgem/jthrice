// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

/** Result that has a value. */
public final class Coup<T, E> extends Result<T, E> {
  /** Coup that contains the given value. */
  public static <T, E> Result<T, E> of(T value) {
    return new Coup<>(value);
  }

  /** Value. */
  public final T value;

  /** Constructor. */
  private Coup(T value) {
    this.value = value;
  }

  @Override
  public boolean is() {
    return true;
  }

  @Override
  public T get() {
    return this.value;
  }

  @Override
  public E error() {
    Bug.unreachable("There is not an error!");
    return null;
  }
}
