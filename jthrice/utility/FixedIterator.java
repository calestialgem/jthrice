// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

/** Iterator over an immutable list. */
public final class FixedIterator<T> {
  /** Iterator over the given list to the given index. */
  public static <T> Maybe<FixedIterator<T>> of(FixedList<T> iterated,
    int index) {
    if (iterated.exists(index)) {
      return Some.of(new FixedIterator<>(iterated, index));
    }
    return None.of();
  }

  /** Iterator to the first element of the given list. */
  public static <T> Maybe<FixedIterator<T>> ofFirst(FixedList<T> iterated) {
    return FixedIterator.of(iterated, 0);
  }

  /** Iterator to the last element of the given list. */
  public static <T> Maybe<FixedIterator<T>> ofLast(FixedList<T> iterated) {
    return FixedIterator.of(iterated, iterated.size() - 1);
  }

  /** List that is iterated over. */
  public final FixedList<T> iterated;
  /** Index of the iterated element. */
  public final int          index;

  /** Constructor. */
  private FixedIterator(FixedList<T> iterated, int index) {
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
  public final <Base extends T> Maybe<Base>
    cast(Class<? extends Base>... deriveds) {
    for (var derived : deriveds) {
      if (derived.isInstance(this.get())) {
        return Some.of((Base) this.get());
      }
    }
    return None.of();
  }

  /** Iterator to the next element. */
  public Maybe<FixedIterator<T>> next() {
    return FixedIterator.of(this.iterated, this.index + 1);
  }

  /** Iterator to the previous element. */
  public Maybe<FixedIterator<T>> previous() {
    return FixedIterator.of(this.iterated, this.index - 1);
  }
}