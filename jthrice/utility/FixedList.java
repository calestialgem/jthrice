// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.*;
import java.util.stream.*;

/** Immutable list. */
public final class FixedList<T> {
  /** List of the given string. */
  public static FixedList<Character> ofString(String string) {
    var list = new ArrayList<Character>();
    for (char c : string.toCharArray()) {
      list.add(c);
    }
    return FixedList.of(list);
  }

  /** Immutable copy of the given collection. */
  public static <T> FixedList<T> ofCopy(Collection<T> list) {
    return FixedList.of(List.copyOf(list));
  }

  /** Immutable view of the given list. */
  public static <T> FixedList<T> of(List<T> elements) {
    return new FixedList<>(elements);
  }

  /** Underlying mutable list. */
  private final List<T> elements;

  /** Constructor. */
  private FixedList(List<T> elements) {
    this.elements = elements;
  }

  /** Amount of elements. */
  public int size() {
    return this.elements.size();
  }

  /** Whether there is an element at the given index. */
  public boolean exists(int index) {
    return index >= 0 && index < this.size();
  }

  /** Element at the given index. */
  public T at(int index) {
    Bug.check(this.exists(index), "Unexisting element!");
    return this.elements.get(index);
  }

  /** Element at the given index from the end. */
  public T atEnd(int index) {
    return this.at(this.size() - 1 - index);
  }

  /** Stream of the elements. */
  public Stream<T> stream() {
    return this.elements.stream();
  }
}
