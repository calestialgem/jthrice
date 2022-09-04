// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.function.Supplier;

/** Container that maybe has a value. */
public sealed abstract class Maybe<T> permits Some<T>, None<T> {
  /** Maybe that contains the given value. */
  public static <T> Maybe<T> of(T value) {
    return Some.of(value);
  }

  /** Maybe that does not contain a value. */
  public static <T> Maybe<T> of() {
    return None.of();
  }

  /** First some or none. */
  @SafeVarargs
  public static <T> Maybe<T> or(Supplier<Maybe<T>>... suppliers) {
    for (var supplier : suppliers) {
      var maybe = supplier.get();
      if (maybe.is()) {
        return maybe;
      }
    }
    return of();
  }

  /** Whether there is a value. */
  public abstract boolean is();

  /** Whether there is not a value. */
  public boolean not() {
    return !is();
  }

  /** Value. */
  public abstract T get();
}
