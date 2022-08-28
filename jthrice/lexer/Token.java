// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;
import java.util.Optional;

import jthrice.exception.Bug;

/** Smallest meaningful group of characters in a source. */
public class Token {
    /** Type of a token. */
    public static enum Type {
        PLUS, MINUS, STAR, FORWARD_SLASH,
        NUMBER, IDENTIFIER;

        /** Token type of the given character if its a mark. */
        public static Optional<Type> of(char character) {
            final String MARKS = "+-*/";
            int index = MARKS.indexOf(character);
            if (index == -1) {
                return Optional.empty();
            }
            Bug.check(index < values().length, "The marks in the string are more than the token types!");
            return Optional.of(values()[index]);
        }
    }

    /** Type. */
    public final Type type;
    /** Parsed value. */
    public final Object value;
    /** Portion in the source. */
    public final Portion portion;

    public Token(Type type, Object value, Portion portion) {
        this.type = type;
        this.value = value;
        this.portion = portion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(portion, type, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Token)) {
            return false;
        }
        Token other = (Token) obj;
        return Objects.equals(portion, other.portion) && type == other.type && Objects.equals(value, other.value);
    }
}
