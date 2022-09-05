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
  public boolean exists() {
    return false;
  }

  @Override
  public T get() {
    Bug.unreachable("There is not a value!");
    return null;
  }

  @Override
  public <U extends T> T get(U fallback) {
    return fallback;
  }

  @Override
  public E error() {
    return this.error;
  }

  @Override
  public Result<T, E> use(Consumer<? super T> user) {
    return this;
  }

  @Override
  public Result<T, E> use(Consumer<? super T> user,
    Consumer<? super E> fallback) {
    fallback.accept(this.error);
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
    return (Result<U, E>) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Result<U, E>
    bind(Function<? super T, Result<? extends U, ? extends E>> binder) {
    return (Result<U, E>) this;
  }
}
