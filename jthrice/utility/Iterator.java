// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.Objects;
import java.util.Optional;

/** Iterator over an immutable list. */
public class Iterator<Element> {
    /** Iterator over the given list to the given index. */
    public static <Element> Optional<Iterator<Element>> of(List<Element> iterated, int index) {
        if (iterated.exists(index)) {
            return Optional.of(new Iterator<>(iterated, index));
        }
        return Optional.empty();
    }

    /** Iterator to the first element of the given list. */
    public static <Element> Optional<Iterator<Element>> ofFirst(List<Element> iterated) {
        return of(iterated, 0);
    }

    /** Iterator to the last element of the given list. */
    public static <Element> Optional<Iterator<Element>> ofLast(List<Element> iterated) {
        return of(iterated, iterated.size() - 1);
    }

    /** List that is iterated over. */
    public final List<Element> iterated;
    /** Index of the iterated element. */
    public final int index;

    public Iterator(List<Element> iterated, int index) {
        Bug.check(iterated.exists(index), "Unexisting iterator!");
        this.iterated = iterated;
        this.index = index;
    }

    /** Iterated element. */
    public Element get() {
        return iterated.at(index);
    }

    /** Iterated element after its casted to base of the given types. */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public final <Base extends Element> Optional<Base> cast(Class<? extends Base>... deriveds) {
        for (var derived : deriveds) {
            if (derived.isInstance(get())) {
                return Optional.of((Base) get());
            }
        }
        return Optional.empty();
    }

    /** Iterator to the next element. */
    public Optional<Iterator<Element>> next() {
        return of(iterated, index + 1);
    }

    /** Iterator to the previous element. */
    public Optional<Iterator<Element>> previous() {
        return of(iterated, index - 1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, iterated);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Iterator)) {
            return false;
        }
        Iterator other = (Iterator) obj;
        return index == other.index && Objects.equals(iterated, other.iterated);
    }
}
