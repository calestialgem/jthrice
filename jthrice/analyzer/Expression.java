// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.Bug;
import jthrice.lexer.Token;

/** Directives to the computer that lead to a value. */
public sealed abstract class Expression
        extends Semantic permits Expression.Literal, Expression.Reference, Expression.Unary, Expression.Binary {
    /** Immediate value. */
    public static final class Literal extends Expression {
        /** Value. */
        public final Token value;

        public Literal(Type type, Token value) {
            super(type);
            this.value = value;
        }
    }

    /** Accessing the value of a variable. */
    public static final class Reference extends Expression {
        /** Refered variable. */
        public final Variable refered;

        public Reference(Variable refered) {
            super(refered.type);
            this.refered = refered;
        }
    }

    /** Value from operating on an expression. */
    public static final class Unary extends Expression {
        /** Operator. */
        public final Token operator;
        /** Operand. */
        public final Expression operand;

        public Unary(Token operator, Expression operand) {
            super(operand.type);
            this.operator = operator;
            this.operand = operand;
        }
    }

    /** Value from operating on two expressions. */
    public static final class Binary extends Expression {
        /** Operator. */
        public final Token operator;
        /** Left operand. */
        public final Expression left;
        /** Right operand. */
        public final Expression right;

        public Binary(Token operator, Expression left, Expression right) {
            super(left.type);
            this.operator = operator;
            this.left = left;
            this.right = right;
            Bug.check(left.type.equals(right.type), "The types in the binary operation are not the same!");
        }
    }

    /** Resulting type. */
    public final Type type;

    public Expression(Type type) {
        this.type = type;
    }
}
