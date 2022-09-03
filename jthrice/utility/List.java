// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.ArrayList;
import java.util.stream.Stream;

/** Immutable list. */
public final class List<T> {
  /** List of the given stirng. */
  public static List<Character> ofString(String string) {
    var list = new ArrayList<Character>();
    for (char c : string.toCharArray()) {
      list.add(c);
    }
    return new List<>(list);
  }

  /** Immutable copy of the given list. */
  public static <T> List<T> of(java.util.List<T> list) {
    return new List<>(new ArrayList<>(list));
  }

  /** Underlying mutable list. */
  private final ArrayList<T> elements;

  public List(ArrayList<T> elements) {
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

  /** Stream of the elements. */
  public Stream<T> stream() {
    return this.elements.stream();
  }
}
