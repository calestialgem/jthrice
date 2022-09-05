// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/** Container that maybe has a value. */
public sealed abstract class Maybe<T> permits Some<T>, None<T> {
  /** Supply the contained value of the given source to the given binder, if it
   * exists. Returns the result of the binder, if it exists. Otherwise fallsback
   * to the given source. */
  public static <T, U extends T, V extends T, W extends V> Maybe<T>
    tryBind(Maybe<W> source, Function<? super V, Maybe<? extends U>> binder) {
    return or(() -> source.bind(binder), () -> source);
  }

  /** Supply the contained values of the given first and second sources to the
   * given bimapper, if they exist. */
  public static <T, U, V> Maybe<V> bimap(Maybe<T> first, Maybe<U> second,
    BiFunction<? super T, ? super U, ? extends V> bimapper) {
    return first.bind(t -> second.map(u -> bimapper.apply(t, u)));
  }

  /** Supply the contained values of the given first and second sources to the
   * given bimapper, if they exist. Returns the given fallback, if they do not
   * exist. */
  public static <T, U, V> V bimap(Maybe<T> first, Maybe<U> second,
    BiFunction<? super T, ? super U, ? extends V> bimapper, V fallback) {
    return get(first.bind(t -> second.map(u -> bimapper.apply(t, u))),
      fallback);
  }

  /** Supply the contained values of the given first and second sources to the
   * given bimapper, if they exist. Runs the given fallback and returns it is
   * output, if they do not exist. */
  public static <T, U, V> V bimap(Maybe<T> first, Maybe<U> second,
    BiFunction<? super T, ? super U, ? extends V> bimapper,
    Supplier<? extends V> fallback) {
    return get(first.bind(t -> second.map(u -> bimapper.apply(t, u))),
      fallback.get());
  }

  /** Supply the contained values of the given first and second sources to the
   * given bibinder, if they exist. Returns the output after flatening it. */
  public static <T, U, V> Maybe<V> bibind(Maybe<T> first, Maybe<U> second,
    BiFunction<? super T, ? super U, Maybe<? extends V>> bibinder) {
    return first.bind(t -> second.bind(u -> bibinder.apply(t, u)));
  }

  /** First source that contains a value, if any, from the given sources. */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T> Maybe<T> or(Supplier<Maybe<? extends T>>... sources) {
    return (Maybe<T>) flaten(of(
      Stream.of(sources).map(Supplier::get).filter(Maybe::exists).findFirst()));
  }

  /** Contained maybe in the given source, if it exists. */
  public static <T> Maybe<T> flaten(Maybe<Maybe<T>> source) {
    return source.get(None.of());
  }

  /** Contained value of the given source, if it exists. Returns the given
   * fallback, if it does not exist. */
  public static <T, U extends T> T get(Maybe<U> source, T fallback) {
    return switch (source) {
      case Some<U> some -> some.value;
      case None<U> none -> fallback;
    };
  }

  /** Maybe of the given optional source. */
  public static <T> Maybe<T> of(Optional<T> source) {
    if (source.isEmpty()) {
      return None.of();
    }
    return Some.of(source.get());
  }

  /** Whether contains a value. */
  public abstract boolean exists();

  /** Whether does not contain a value. */
  public boolean empty() {
    return !exists();
  }

  /** Contained value. The caller should guarantee that a value exists. */
  public abstract T get();

  /** Contained value, if it exists. Returns the given fallback, if it does not
   * exist. */
  public abstract <U extends T> T get(U fallback);

  /** Contained value, if it exists. Runs the given fallback and returns it is
   * output, if it does not exist. */
  public abstract T get(Supplier<? extends T> fallback);

  /** Supply the contained value to the given user, if it exists. Returns
   * this. */
  public abstract Maybe<T> use(Consumer<? super T> user);

  /** Supply the contained value to the given user, if it exists. Runs the given
   * fallback, if it does not exist. Returns this. */
  public abstract Maybe<T> use(Consumer<? super T> user, Runnable fallback);

  /** Supply the contained value to the given mapper, if it exists. */
  public abstract <U> Maybe<U> map(Function<? super T, ? extends U> mapper);

  /** Supply the contained value to the given mapper, if it exists. Returns the
   * given fallback, if it does not exist. */
  public abstract <U> U map(Function<? super T, ? extends U> mapper,
    U fallback);

  /** Supply the contained value to the given mapper, if it exists. Runs the
   * given fallback and returns it is output, if it does not exist. */
  public abstract <U> U map(Function<? super T, ? extends U> mapper,
    Supplier<? extends U> fallback);

  /** Supply the contained value to the given binder, if it exists. Returns the
   * output after flatening it. */
  public abstract <U> Maybe<U>
    bind(Function<? super T, Maybe<? extends U>> binder);
}
