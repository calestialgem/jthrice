// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.Optional;

import jthrice.lexer.Identifier;
import jthrice.lexer.Keyword;
import jthrice.lexer.Mark;
import jthrice.lexer.Number;

/** Symbols that result in a value. */
public sealed abstract class Expression
        extends Symbol permits Expression.Literal, Expression.Reference, Expression.Unary, Expression.Binary {
    /** From the top of the given stack. */
    public static Optional<Result> of(Stack stack) {
        return Result.max(Literal.of(stack), Reference.of(stack), Unary.of(stack), Binary.of(stack));
    }

    /** Hard coded value. */
    public static final class Literal extends Expression {
        /** From the top of the given stack. */
        public static Optional<Result> of(Stack stack) {
            Optional<Terminal> value = stack.topTerminal(0, Number.class, Keyword.I1.class, Keyword.I2.class,
                    Keyword.I4.class, Keyword.I8.class, Keyword.IX.class, Keyword.U1.class, Keyword.U2.class,
                    Keyword.U4.class, Keyword.U8.class, Keyword.UX.class, Keyword.F4.class, Keyword.F8.class);
            if (value.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new Result(new Literal(value.get()), 1));
        }

        /** Token that carries the value. */
        public final Terminal value;

        public Literal(Terminal value) {
            this.value = value;
        }
    }

    /** Value from another object. */
    public static final class Reference extends Expression {
        /** From the top of the given stack. */
        public static Optional<Result> of(Stack stack) {
            Optional<Terminal> name = stack.topTerminal(0, Identifier.class);
            if (name.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new Result(new Reference(name.get()), 1));
        }

        /** Name of the refered object. */
        public final Terminal name;

        public Reference(Terminal name) {
            this.name = name;
        }
    }

    /** Result of a unary operation. */
    public static final class Unary extends Expression {
        /** From the top of the given stack. */
        public static Optional<Result> of(Stack stack) {
            Optional<Terminal> operator = stack.topTerminal(1, Mark.Plus.class, Mark.Minus.class);
            Optional<Expression> operand = stack.top(0, Expression.class);
            if (operator.isEmpty() || operand.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new Result(new Unary(operator.get(), operand.get()), 2));
        }

        /** Operator. */
        public final Terminal operator;
        /** Operand. */
        public final Expression operand;

        public Unary(Terminal operator, Expression operand) {
            this.operator = operator;
            this.operand = operand;
        }
    }

    /** Result of a binary operation. */
    public static final class Binary extends Expression {
        /** From the top of the given stack. */
        public static Optional<Result> of(Stack stack) {
            Optional<Terminal> operator = stack.topTerminal(1, Mark.Plus.class, Mark.Minus.class, Mark.Star.class,
                    Mark.ForwardSlash.class, Mark.Percent.class);
            Optional<Expression> left = stack.top(0, Expression.class);
            Optional<Expression> right = stack.top(2, Expression.class);
            if (operator.isEmpty() || left.isEmpty() || right.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new Result(new Binary(operator.get(), left.get(), right.get()), 3));
        }

        /** Operator. */
        public final Terminal operator;
        /** Left operand. */
        public final Expression left;
        /** Right operand. */
        public final Expression right;

        public Binary(Terminal operator, Expression left, Expression right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }
    }
}
