// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/** Immutable list. */
public class List<Element> {
    /** Immutable copy of the given list. */
    public static <Element> List<Element> of(java.util.List<Element> list) {
        return new List<>(new ArrayList<>(list));
    }

    /** List of the given array. */
    @SafeVarargs
    public static <Element> List<Element> of(Element... array) {
        return of(java.util.List.of(array));
    }

    /** Underlying mutable list. */
    private final ArrayList<Element> elements;

    public List(ArrayList<Element> elements) {
        this.elements = elements;
    }

    /** Amount of elements. */
    public int size() {
        return elements.size();
    }

    /** Element at the given index. */
    public Optional<Element> get(int index) {
        if (index < 0 || index >= size()) {
            return Optional.empty();
        }
        return Optional.of(elements.get(index));
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        List other = (List) obj;
        return Objects.equals(elements, other.elements);
    }
}
