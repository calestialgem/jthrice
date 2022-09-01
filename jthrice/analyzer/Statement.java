// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

/** Directive to the computer. */
public sealed abstract class Statement extends Semantic permits Statement.Definition {
    /** Definition of a variable. */
    public static final class Definition extends Statement {
        /** Defined variable. */
        public final Variable defined;
        /** Defined value. */
        public final Expression value;

        public Definition(Variable defined, Expression value) {
            this.defined = defined;
            this.value = value;
        }
    }
}
