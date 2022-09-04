// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.function.*;

/** Maybe that does not contain a value. */
public final class None<T> extends Maybe<T> {
  /** Shared instance. */
  private static final None<?> INSTANCE = new None<>();

  /** None. */
  @SuppressWarnings("unchecked")
  public static <T> Maybe<T> of() {
    return (None<T>) None.INSTANCE;
  }

  /** Constructor. */
  private None() {
  }

  @Override
  public boolean exists() {
    return false;
  }

  @Override
  public <U extends T> T get(U fallback) {
    return fallback;
  }

  @Override
  public T get(Supplier<? extends T> fallback) {
    return fallback.get();
  }

  @Override
  public Maybe<T> use(Consumer<? super T> user) {
    return this;
  }

  @Override
  public Maybe<T> use(Consumer<? super T> user, Runnable fallback) {
    fallback.run();
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Maybe<U> map(Function<? super T, ? extends U> mapper) {
    return (Maybe<U>) this;
  }

  @Override
  public <U> U map(Function<? super T, ? extends U> mapper, U fallback) {
    return fallback;
  }

  @Override
  public <U> U map(Function<? super T, ? extends U> mapper,
    Supplier<? extends U> fallback) {
    return fallback.get();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Maybe<U> bind(Function<? super T, Maybe<? extends U>> binder) {
    return (Maybe<U>) this;
  }
}
