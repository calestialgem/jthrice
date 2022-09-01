// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.Arrays;

/** Semantic representation of a Thrice program. */
public final class Program extends Semantic {
    /** Statements in the program. */
    private final Statement[] statements;

    public Program(Statement[] statements) {
        this.statements = statements;
    }

    /** Statements in the program. */
    public Statement[] statements() {
        return Arrays.copyOf(statements, statements.length);
    }
}
