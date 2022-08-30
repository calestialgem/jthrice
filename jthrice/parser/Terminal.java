// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import jthrice.lexer.Token;

/** Symbol that do not have other symbols under it, only a token. */
public final class Terminal extends Symbol {
    /** Token. */
    public final Token token;

    public Terminal(Token token) {
        this.token = token;
    }
}
