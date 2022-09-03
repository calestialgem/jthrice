// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.utility.List;

/** An existince in a Thrice program. */
public sealed abstract class Entity permits Entity.Program, Entity.Statement, Entity.Expression {
    /** Root entity, which represents the whole program. */
    public static final class Program extends Entity {
        /** Statements in the program. */
        public final List<Statement> statements;

        public Program(List<Statement> statements) {
            this.statements = statements;
        }
    }

    /** Directives that are executed by the computer in order. */
    public static sealed abstract class Statement extends Entity permits Statement.Definition {
        /** Creation of a variable. */
        public static final class Definition extends Statement {
            /** Variable name. */
            public final Name name;
            /** Varible type. */
            public final Type type;
            /** Variable value. */
            public final Expression value;

            public Definition(Name name, Type type, Expression value) {
                this.name = name;
                this.type = type;
                this.value = value;
            }
        }
    }

    /** Calculations and actions that lead to a value. */
    public static sealed abstract class Expression
            extends Entity permits Expression.Primary, Expression.Unary, Expression.Binary {
        /** Independent expression. */
        public static sealed abstract class Primary extends Expression permits Primary.Literal, Primary.Access {
            /** Hard coded value. */
            public static final class Literal extends Primary {
                /** Value. */
                public final Object value;

                public Literal(Type type, Object value) {
                    super(type);
                    this.value = value;
                }
            }

            /** Value of a variable. */
            public static final class Access extends Primary {
                /** Name of the accessed variable. */
                public final Name name;

                public Access(Type type, Name name) {
                    super(type);
                    this.name = name;
                }
            }

            public Primary(Type type) {
                super(type);
            }
        }

        /** Operation on an expression. */
        public static final class Unary extends Expression {
            /** Operator. */
            public final String operator;
            /** Operand. */
            public final Expression operand;

            public Unary(Type type, String operator, Expression operand) {
                super(type);
                this.operator = operator;
                this.operand = operand;
            }
        }

        public static final class Binary extends Expression {
            /** Operator. */
            public final String operator;
            /** Left operand. */
            public final Expression left;
            /** Right operand. */
            public final Expression right;

            public Binary(Type type, String operator, Expression left, Expression right) {
                super(type);
                this.operator = operator;
                this.left = left;
                this.right = right;
            }
        }

        /** Type of the expression. */
        public final Type type;

        public Expression(Type type) {
            this.type = type;
        }
    }
}
