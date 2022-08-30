// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.Optional;

/** Hierarchical collection of tokens. */
public sealed abstract class Symbol permits Terminal, Start, Statement, Decleration, Expression {
    /** Result of parsing a symbol. */
    public static class Result {
        /** Maximum of all the given results. */
        @SafeVarargs
        public static Optional<Result> max(Optional<Result>... results) {
            Optional<Result> max = Optional.empty();
            for (Optional<Result> result : results) {
                if (result.isEmpty()) {
                    continue;
                }
                if (max.isEmpty() || result.get().consumed > max.get().consumed) {
                    result = max;
                }
            }
            return max;
        }

        /** Resulting symbol. */
        public final Symbol symbol;
        /** Amount of symbols used in the symbols creation. */
        public final int consumed;

        public Result(Symbol symbol, int consumed) {
            this.symbol = symbol;
            this.consumed = consumed;
        }
    }

    /** From the top of the given stack. */
    public static Optional<Result> of(Stack stack) {
        return Result.max(Start.of(stack), Statement.of(stack), Decleration.of(stack), Expression.of(stack));
    }
}
