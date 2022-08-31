// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;
import java.util.Optional;

/** Symbol name in definition or reference. */
public final class Identifier extends Token {
    /** Identifier at the given location. */
    public static Optional<Token> of(Location first) {
        Location current = first;
        if ((current.get() < 'a' || current.get() > 'z') && (current.get() < 'A' || current.get() > 'Z')
                && current.get() != '_') {
            return Optional.empty();
        }

        StringBuilder builder = new StringBuilder();
        while ((current.get() >= '0' && current.get() <= '9') || (current.get() >= 'a' && current.get() <= 'z')
                || (current.get() >= 'A' && current.get() <= 'Z') || current.get() == '_') {
            builder.append(current.get());
            Optional<Location> next = current.next();
            if (next.isEmpty()) {
                break;
            }
            current = next.get();
        }

        String value = builder.toString();
        Portion portion = new Portion(first, current.previous().get());
        return Optional.of(new Identifier(portion, value));
    }

    /** Value of the identifier. */
    public final String value;

    public Identifier(Portion portion, String value) {
        super(portion);
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Identifier)) {
            return false;
        }
        Identifier other = (Identifier) obj;
        return Objects.equals(value, other.value);
    }
}
