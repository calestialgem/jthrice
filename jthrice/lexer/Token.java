// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

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
}
