// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.HashMap;
import java.util.stream.Stream;

/** Immutable map. */
public final class Map<K, V> {
  /** Immutable copy of the given map. */
  public static <K, V> Map<K, V> of(java.util.Map<K, V> map) {
    return new Map<>(new HashMap<>(map));
  }

  /** Underlying mutable map. */
  private final HashMap<K, V> entries;

  public Map(HashMap<K, V> entries) {
    this.entries = entries;
  }

  /** Amount of entries. */
  public int size() {
    return this.entries.size();
  }

  /** Whether there is a mapping from the given key. */
  public boolean exists(K key) {
    return this.entries.containsKey(key);
  }

  /** Value that maps to the given key. */
  public V at(K key) {
    return this.entries.get(key);
  }

  /** Stream of the entries. */
  public Stream<java.util.Map.Entry<K, V>> stream() {
    return this.entries.entrySet().stream();
  }

  /** Stream of the keys. */
  public Stream<K> keyStream() {
    return this.entries.keySet().stream();
  }

  /** Stream of the values. */
  public Stream<V> valueStream() {
    return this.entries.values().stream();
  }
}
