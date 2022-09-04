// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.function.*;

/** Container that can have a value or an error. */
public sealed abstract class Result<T, E> permits Coup<T, E>, Dud<T, E> {
  /** Map the contained value using the given mapper, if its there. If error is
   * contained, propagates it. */
  public abstract <U> Result<U,E> map(Function<? super T, ? extends U> mapper);
}
