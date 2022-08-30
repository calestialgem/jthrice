// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Optional;

/** Smallest meaningful group of characters in a source. */
public sealed abstract class Token permits Mark, Number, Keyword, Identifier {
    /** Token at the given location. */
    public static Optional<Token> of(Location first) {
        Optional<Token> mark = Mark.of(first);
        if (mark.isPresent()) {
            return mark;
        }
        Optional<Token> number = Number.of(first);
        if (number.isPresent()) {
            return number;
        }
        Optional<Token> identifier = Identifier.of(first);
        if (identifier.isPresent()) {
            Optional<Token> keyword = Keyword.of((Identifier) identifier.get());
            if (keyword.isPresent()) {
                return keyword;
            }
            return identifier;
        }
        return Optional.empty();
    }

    /** Portion in the source. */
    public final Portion portion;

    public Token(Portion portion) {
        this.portion = portion;
    }
}
