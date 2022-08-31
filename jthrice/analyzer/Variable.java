// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.Identifier;

/** Chunk of memory with a type. */
public final class Variable extends Entity {
    /** Name of the variable. */
    public final Identifier name;
    /** Type of the variable. */
    public final Type type;

    public Variable(Identifier name, Type type) {
        this.name = name;
        this.type = type;
    }
}
