// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

/** Maybe that does not contain a value. */
public final class None<T> extends Maybe<T> {
  /** Shared instance. */
  private static final None<?> INSTANCE = new None<>();

  /** None. */
  @SuppressWarnings("unchecked")
  public static <T> Maybe<T> of() {
    return (None<T>) INSTANCE;
  }

  /** Constructor. */
  private None() {
  }

  @Override
  public boolean is() {
    return false;
  }

  @Override
  public T get() {
    Bug.unreachable("There is not a value!");
    return null;
  }
}
