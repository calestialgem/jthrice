// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.Optional;

import jthrice.lexer.Mark;

/** Commands that are executed by the computer in order. */
public sealed abstract class Statement extends Symbol permits Statement.Definition {
    /** From the top of the given stack. */
    public static Optional<Result> of(Stack stack) {
        return Result.max(Definition.of(stack));
    }

    /** Creation of an object. */
    public static final class Definition extends Statement {
        /** From the top of the given stack. */
        public static Optional<Result> of(Stack stack) {
            Optional<Decleration> decleration = stack.top(5, Decleration.class);
            Optional<Terminal> separator = stack.topTerminal(4, Mark.Colon.class);
            Optional<Expression> type = stack.top(3, Expression.class);
            Optional<Terminal> assignment = stack.topTerminal(2, Mark.Equal.class);
            Optional<Expression> value = stack.top(1, Expression.class);
            Optional<Terminal> end = stack.topTerminal(0, Mark.Semicolon.class);
            if (decleration.isEmpty() || separator.isEmpty() || type.isEmpty() || assignment.isEmpty()
                    || value.isEmpty() || end.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new Result(new Definition(decleration.get(), separator.get(), type.get(),
                    assignment.get(), value.get(), end.get()), 6));
        }

        /** Decleration. */
        public final Decleration decleration;
        /** Separator of type and name. */
        public final Terminal separator;
        /** Type expression. */
        public final Expression type;
        /** Assignment operator. */
        public final Terminal assignment;
        /** Value expression. */
        public final Expression value;
        /** End of the statement. */
        public final Terminal end;

        public Definition(Decleration decleration, Terminal separator, Expression type, Terminal assignment,
                Expression value, Terminal end) {
            this.decleration = decleration;
            this.separator = separator;
            this.type = type;
            this.assignment = assignment;
            this.value = value;
            this.end = end;
        }
    }
}
