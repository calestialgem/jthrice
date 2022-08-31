// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import jthrice.Bug;
import jthrice.lexer.Token;

/** Location of a token in a list of tokens. */
public class Location {
    /** First location in the tokens. */
    public static Optional<Location> ofFirst(Token[] tokens) {
        if (tokens.length > 0) {
            return Optional.of(new Location(tokens, 0));
        }
        return Optional.empty();
    }

    /** List that token is in. */
    private final Token[] tokens;
    /** Index of the token. */
    public final int index;

    public Location(Token[] tokens, int index) {
        Bug.check(index >= 0, "Index is negative!");
        Bug.check(index < tokens.length, "Index out of bounds!");
        this.tokens = tokens;
        this.index = index;
    }

    /** Token at the location. */
    public Token get() {
        return tokens[index];
    }

    /** Token at the location if its of the given type. */
    @SuppressWarnings("unchecked")
    public <T extends Token> Optional<T> cast(Class<T> type) {
        if (type.isInstance(get())) {
            return Optional.of((T) get());
        }
        return Optional.empty();
    }

    /** Token at the location if its of one of the given types. */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public final <T extends Token> Optional<T> check(Class<? extends T>... types) {
        for (var type : types) {
            if (type.isInstance(get())) {
                return Optional.of((T) get());
            }
        }
        return Optional.empty();
    }

    /** Location after this one. */
    public Optional<Location> next() {
        if (index + 1 < tokens.length) {
            return Optional.of(new Location(tokens, index + 1));
        }
        return Optional.empty();
    }

    /** Location before this one. */
    public Optional<Location> previous() {
        if (index > 0) {
            return Optional.of(new Location(tokens, index - 1));
        }
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(tokens);
        result = prime * result + Objects.hash(index);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Location)) {
            return false;
        }
        Location other = (Location) obj;
        return index == other.index && Arrays.equals(tokens, other.tokens);
    }
}
