// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.Objects;
import java.util.Optional;

/** Name of a variable. */
public final class Name {
    /** Namespace the name is in. */
    public final Optional<Name> space;
    /** String value of the name. */
    public final String value;

    public Name(Optional<Name> space, String value) {
        this.space = space;
        this.value = value;
    }

    public Name(String value) {
        this(Optional.empty(), value);
    }

    public Name(Name space, String value) {
        this(Optional.of(space), value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(space, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Name)) {
            return false;
        }
        Name other = (Name) obj;
        return Objects.equals(space, other.space) && Objects.equals(value, other.value);
    }
}
