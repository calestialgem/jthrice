// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;

/** Smallest meaningful group of characters in a source. */
public class Token {
    /** Type. */
    public final TokenType type;
    /** Parsed value. */
    public final Object value;
    /** Portion in the source. */
    public final Portion portion;

    /** Initialize with the given type, value and portion. */
    public Token(TokenType type, Object value, Portion portion) {
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
