// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.function.*;

/** Container that maybe has a value. */
public sealed abstract class Maybe<T> permits Some<T>, None<T> {
  /** Contained value, if its there. Returns the given fallback, if its not
   * there. */
  public abstract <U extends T> T get(U fallback);

  /** If does not contains a value, gets from the given supplier and returns
   * that. Otherwise returns this. */
  public abstract Maybe<T> or(Supplier<Maybe<? extends T>> supplier);

  /** Supply the contained value to the given user, if its there. Returns
   * this. */
  public abstract Maybe<T> use(Consumer<? super T> user);

  /** Supply the contained value to the given user, if its there. Runs the given
   * fallback, if its not there. Returns thsi. */
  public abstract Maybe<T> use(Consumer<? super T> user, Runnable fallback);

  /** Supply the contained value to the given mapper, if its there. */
  public abstract <U> Maybe<U> map(Function<? super T, ? extends U> mapper);

  /** Supply the contained value to the given binder, if its there. Returns the
   * output after flatening it. */
  public abstract <U> Maybe<U>
    bind(Function<? super T, Maybe<? extends U>> binder);
}
