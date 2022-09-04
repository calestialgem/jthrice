// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.function.*;

/** Result that has an error. */
public final class Dud<T, E> extends Result<T, E> {
  /** Dud that contains the given error. */
  public static <T, E> Result<T, E> of(E error) {
    return new Dud<>(error);
  }

  /** Error. */
  public final E error;

  /** Constructor. */
  private Dud(E error) {
    this.error = error;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
    return (Result<U, E>) this;
  }
}
