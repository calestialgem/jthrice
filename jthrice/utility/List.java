// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/** Immutable list. */
public final class List<T> {
  /** List of the given string. */
  public static List<Character> ofString(String string) {
    var list = new ArrayList<Character>();
    for (char c : string.toCharArray()) {
      list.add(c);
    }
    return List.of(list);
  }

  /** Immutable copy of the given collection. */
  public static <T> List<T> ofCopy(Collection<T> list) {
    return List.of(java.util.List.copyOf(list));
  }

  /** Immutable view of the given list. */
  public static <T> List<T> of(java.util.List<T> elements) {
    return new List<>(elements);
  }

  /** Underlying mutable list. */
  private final java.util.List<T> elements;

  /** Constructor. */
  private List(java.util.List<T> elements) {
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
