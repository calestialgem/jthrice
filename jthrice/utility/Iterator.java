// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.Objects;

/** Iterator over an immutable list. */
public final class Iterator<T> {
  /** Iterator over the given list to the given index. */
  public static <T> Result<Iterator<T>> of(List<T> iterated, int index) {
    if (iterated.exists(index)) {
      return Result.of(new Iterator<>(iterated, index));
    }
    return Result.ofUnexisting();
  }

  /** Iterator to the first element of the given list. */
  public static <T> Result<Iterator<T>> ofFirst(List<T> iterated) {
    return Iterator.of(iterated, 0);
  }

  /** Iterator to the last element of the given list. */
  public static <T> Result<Iterator<T>> ofLast(List<T> iterated) {
    return Iterator.of(iterated, iterated.size() - 1);
  }

  /** List that is iterated over. */
  public final List<T> iterated;
  /** Index of the iterated element. */
  public final int     index;

  public Iterator(List<T> iterated, int index) {
    Bug.check(iterated.exists(index), "Unexisting iterator!");
    this.iterated = iterated;
    this.index    = index;
  }

  /** Iterated element. */
  public T get() {
    return this.iterated.at(this.index);
  }

  /** Iterated element after its casted to base of the given types. */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public final <Base extends T> Result<Base>
    cast(Class<? extends Base>... deriveds) {
    for (var derived : deriveds) {
      if (derived.isInstance(this.get())) {
        return Result.of((Base) this.get());
      }
    }
    return Result.ofUnexisting();
  }

  /** Iterator to the next element. */
  public Result<Iterator<T>> next() {
    return Iterator.of(this.iterated, this.index + 1);
  }

  /** Iterator to the previous element. */
  public Result<Iterator<T>> previous() {
    return Iterator.of(this.iterated, this.index - 1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.index, this.iterated);
  }

  @Override
  @SuppressWarnings("rawtypes")
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Iterator other)) {
      return false;
    }
    return this.index == other.index
      && Objects.equals(this.iterated, other.iterated);
  }
}
