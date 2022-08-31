// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.Optional;

import jthrice.lexer.Token;

/** Informing the compiler about an object and its type. */
public final class Decleration extends Symbol {
    /** From the top of the given stack. */
    public static Optional<Result> of(Stack stack) {
        Optional<Terminal> keyword = stack.topTerminal(1, Token.Keyword.Let.class);
        Optional<Terminal> name = stack.topTerminal(0, Token.Identifier.class);
        if (keyword.isEmpty() || name.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Result(new Decleration(keyword.get(), name.get()), 2));
    }

    /** Keyword that marks a decleration. */
    public final Terminal keyword;
    /** Identifier that is the name of the object. */
    public final Terminal name;

    public Decleration(Terminal keyword, Terminal name) {
        this.keyword = keyword;
        this.name = name;
    }
}
