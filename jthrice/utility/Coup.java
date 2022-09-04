// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.function.*;

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
  public Result<T, E> use(Consumer<? super T> user) {
    user.accept(this.value);
    return this;
  }

  @Override
  public Result<T, E> use(Consumer<? super T> user,
    Consumer<? super E> fallback) {
    user.accept(this.value);
    return this;
  }

  @Override
  public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
    return Coup.of(mapper.apply(this.value));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Result<U, E>
    bind(Function<? super T, Result<? extends U, ? extends E>> binder) {
    return (Result<U, E>) binder.apply(this.value);
  }
}
