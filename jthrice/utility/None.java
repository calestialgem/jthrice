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
  public <U extends T> T get(U fallback) {
    return fallback;
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
  @SuppressWarnings("unchecked")
  public Maybe<T> or(Supplier<Maybe<? extends T>> supplier) {
    return (Maybe<T>) supplier.get();
  }
}
