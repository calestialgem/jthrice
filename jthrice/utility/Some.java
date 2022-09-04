// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.function.*;

/** Maybe that contains a value. */
public final class Some<T> extends Maybe<T> {
  /** Some containing the given value. */
  public static <T> Maybe<T> of(T value) {
    return new Some<>(value);
  }

  /** Value. */
  public final T value;

  /** Constructor. */
  private Some(T value) {
    this.value = value;
  }

  @Override
  public boolean exists() {
    return true;
  }

  @Override
  public <U extends T> T get(U fallback) {
    return this.value;
  }

  @Override
  public T get(Supplier<? extends T> fallback) {
    return this.value;
  }

  @Override
  public Maybe<T> use(Consumer<? super T> user) {
    user.accept(this.value);
    return this;
  }

  @Override
  public Maybe<T> use(Consumer<? super T> user, Runnable fallback) {
    user.accept(this.value);
    return this;
  }

  @Override
  public <U> Maybe<U> map(Function<? super T, ? extends U> mapper) {
    return Some.of(mapper.apply(this.value));
  }

  @Override
  public <U> U map(Function<? super T, ? extends U> mapper, U fallback) {
    return mapper.apply(this.value);
  }

  @Override
  public <U> U map(Function<? super T, ? extends U> mapper,
    Supplier<? extends U> fallback) {
    return mapper.apply(this.value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Maybe<U> bind(Function<? super T, Maybe<? extends U>> binder) {
    return (Maybe<U>) binder.apply(this.value);
  }
}
