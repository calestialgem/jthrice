// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Optional;

import jthrice.exception.Bug;

/** Type of a token. */
public enum TokenType {
    PLUS, MINUS, STAR, FORWARD_SLASH,
    NUMBER, IDENTIFIER;

    /** The token type of the given character. */
    public static Optional<TokenType> getPunctuation(char character) {
        final String PUNCTUATIONS = "+-*/";
        int index = PUNCTUATIONS.indexOf(character);
        if (index == -1) {
            return Optional.empty();
        }
        Bug.check(index < values().length, "The punctuations string is longer than the token types!");
        return Optional.of(values()[index]);
    }
}
