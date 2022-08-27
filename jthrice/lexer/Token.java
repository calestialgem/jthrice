// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

/** Smallest meaningful group of characters in a source string. */
public class Token {
    /** Type. */
    TokenType type;
    /** Parsed value. */
    Object value;
    /** Portion in the source string. */
    Portion portion;
}
