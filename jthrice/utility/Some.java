// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

/** Maybe that contains a value. */
public final class Some<T> extends Maybe<T> {
  /** Some containing the given value. */
  public static <T> Some<T> of(T value) {
    return new Some<>(value);
  }

  /** Value. */
  public final T value;

  /** Constructor. */
  private Some(T value) {
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
}
