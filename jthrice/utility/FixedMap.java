// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.*;
import java.util.stream.*;

/** Immutable map. */
public final class FixedMap<K, V> {
  /** Immutable copy of the given map. */
  public static <K, V> FixedMap<K, V> ofCopy(Map<K, V> map) {
    return FixedMap.of(Map.copyOf(map));
  }

  /** Immutable view of the given map. */
  public static <K, V> FixedMap<K, V> of(Map<K, V> map) {
    return FixedMap.of(map);
  }

  /** Underlying mutable map. */
  private final Map<K, V> entries;

  /** Constructor. */
  private FixedMap(Map<K, V> entries) {
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
  public Stream<Map.Entry<K, V>> stream() {
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
