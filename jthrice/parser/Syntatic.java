// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.Arrays;
import java.util.Objects;

import jthrice.lexer.Token;

/** Syntatic objects; hierarchical collection of tokens. */
public sealed abstract class Syntatic permits Syntatic.Source, Syntatic.Statement, Syntatic.Expression {
    /** Root of all the syntatic objects in a source file. */
    public static final class Source extends Syntatic {
        /** Statements under the source file. */
        private final Statement[] statements;
        /** End of the file. */
        public final Token.Mark.EOF eof;

        public Source(Statement[] statements, Token.Mark.EOF eof) {
            this.statements = statements;
            this.eof = eof;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(statements);
            result = prime * result + Objects.hash(eof);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Source)) {
                return false;
            }
            Source other = (Source) obj;
            return Objects.equals(eof, other.eof) && Arrays.equals(statements, other.statements);
        }
    }

    /** Directives that are executed by the computer in order. */
    public static sealed abstract class Statement extends Syntatic permits Statement.Definition {
        /** Creation of a variable. */
        public static final class Definition extends Statement {
            /** Name of the defined variable. */
            public final Token.Identifier name;
            /** Separator of name and type. */
            public final Token.Mark.Colon separator;
            /** Type expression. */
            public final Expression type;
            /** Assignment operator. */
            public final Token.Mark.Equal assignment;
            /** Value expression. */
            public final Expression value;
            /** End of the statement. */
            public final Token.Mark.Semicolon end;

            public Definition(Token.Identifier name, Token.Mark.Colon separator, Expression type,
                    Token.Mark.Equal assignment, Expression value,
                    Token.Mark.Semicolon end) {
                this.name = name;
                this.separator = separator;
                this.type = type;
                this.assignment = assignment;
                this.value = value;
                this.end = end;
            }

            @Override
            public int hashCode() {
                return Objects.hash(assignment, end, name, separator, type, value);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Definition)) {
                    return false;
                }
                Definition other = (Definition) obj;
                return Objects.equals(assignment, other.assignment) && Objects.equals(end, other.end)
                        && Objects.equals(name, other.name) && Objects.equals(separator, other.separator)
                        && Objects.equals(type, other.type) && Objects.equals(value, other.value);
            }
        }
    }

    /** Calculations and actions that lead to a value. */
    public static sealed abstract class Expression
            extends Syntatic permits Expression.Primary, Expression.Unary, Expression.Binary {
        /** Independent expression. */
        public static sealed abstract class Primary extends Expression permits Primary.Literal, Primary.Access {
            /** Hard coded value. */
            public static final class Literal extends Primary {
                /** Token that carries the value. */
                public final Token value;

                public Literal(Token value) {
                    this.value = value;
                }

                @Override
                public int hashCode() {
                    return Objects.hash(value);
                }

                @Override
                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (!(obj instanceof Literal)) {
                        return false;
                    }
                    Literal other = (Literal) obj;
                    return Objects.equals(value, other.value);
                }
            }

            /** Value of a variable. */
            public static final class Access extends Primary {
                /** Name of the accessed variable. */
                public final Token.Identifier name;

                public Access(Token.Identifier name) {
                    this.name = name;
                }

                @Override
                public int hashCode() {
                    return Objects.hash(name);
                }

                @Override
                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (!(obj instanceof Access)) {
                        return false;
                    }
                    Access other = (Access) obj;
                    return Objects.equals(name, other.name);
                }
            }
        }

        /** Operation on an expression. */
        public static final class Unary extends Expression {
            /** Operator. */
            public final Token operator;
            /** Operand. */
            public final Expression operand;

            public Unary(Token operator, Expression operand) {
                this.operator = operator;
                this.operand = operand;
            }

            @Override
            public int hashCode() {
                return Objects.hash(operand, operator);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Unary)) {
                    return false;
                }
                Unary other = (Unary) obj;
                return Objects.equals(operand, other.operand) && Objects.equals(operator, other.operator);
            }
        }

        /** Operation on two expressions. */
        public static final class Binary extends Expression {
            /** Operation. */
            public final Token operation;
            /** Left operand. */
            public final Expression left;
            /** Right operand. */
            public final Expression right;

            public Binary(Token operation, Expression left, Expression right) {
                this.operation = operation;
                this.left = left;
                this.right = right;
            }

            @Override
            public int hashCode() {
                return Objects.hash(left, operation, right);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Binary)) {
                    return false;
                }
                Binary other = (Binary) obj;
                return Objects.equals(left, other.left) && Objects.equals(operation, other.operation)
                        && Objects.equals(right, other.right);
            }
        }
    }
}
