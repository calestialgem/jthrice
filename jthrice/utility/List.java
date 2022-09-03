// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

/** Immutable list. */
public class List<Element> {
    /** List of the given stirng. */
    public static List<Character> ofString(String string) {
        var list = new ArrayList<Character>();
        for (char c : string.toCharArray()) {
            list.add(c);
        }
        return of(list);
    }

    /** Immutable copy of the given list. */
    public static <Element> List<Element> of(java.util.List<Element> list) {
        return new List<>(new ArrayList<>(list));
    }

    /** List of the given array. */
    public static <Element> List<Element> of(Element[] array) {
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

    /** Whether there is an element at the given index. */
    public boolean exists(int index) {
        return index >= 0 && index < size();
    }

    /** Element at the given index. */
    public Element at(int index) {
        Bug.check(exists(index), "Unexisting element!");
        return elements.get(index);
    }

    /** Supply the elements to the given consumer. */
    public void forEach(Consumer<? super Element> consumer) {
        elements.forEach(consumer);
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
