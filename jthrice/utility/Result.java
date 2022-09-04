// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.function.*;

/** Container that can have a value or an error. */
public sealed abstract class Result<T, E> permits Coup<T, E>, Dud<T, E> {
  /** Supply the contained value to the given user, if its there. Returns
   * this. */
  public abstract Result<T, E> use(Consumer<? super T> user);

  /** Supply the contained value to the given user, if its there. If error is
   * contained, supplies it to the given fallback. Returns this. */
  public abstract Result<T, E> use(Consumer<? super T> user,
    Consumer<? super E> fallback);

  /** Supply the contained value to the given mapper, if its there. If error is
   * contained, propagates it. */
  public abstract <U> Result<U, E> map(Function<? super T, ? extends U> mapper);

  /** Supply the contained value to the given binder, if its there. Returns the
   * output after flatening it. */
  public abstract <U> Result<U, E>
    bind(Function<? super T, Result<? extends U, ? extends E>> binder);
}
