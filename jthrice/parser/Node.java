// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.Objects;

import jthrice.lexer.Lexeme;
import jthrice.lexer.Lexeme.Token.ClosingParentheses;
import jthrice.lexer.Lexeme.Token.OpeningParentheses;
import jthrice.utility.List;

/** Hierarchical, context-free, collection of lexemes. */
public sealed abstract class Node permits Node.Program, Node.Statement, Node.Expression {
    /** Root node, which represent the whole program. */
    public static final class Program extends Node {
        /** Statements in the program. */
        public final List<Statement> statements;
        /** End of the file. */
        public final Lexeme.Token.EOF eof;

        public Program(List<Statement> statements, Lexeme.Token.EOF eof) {
            this.statements = statements;
            this.eof = eof;
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();
            statements.forEach(statement -> builder.append(statement).append(System.lineSeparator()));
            return builder.toString();
        }

        @Override
        public int hashCode() {
            return Objects.hash(eof, statements);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Program)) {
                return false;
            }
            Program other = (Program) obj;
            return Objects.equals(eof, other.eof) && Objects.equals(statements, other.statements);
        }
    }

    /** Directives that are executed by the computer in order. */
    public static sealed abstract class Statement extends Node permits Statement.Definition {
        /** Creation of a variable. */
        public static final class Definition extends Statement {
            /** Name of the defined variable. */
            public final Lexeme.Identifier name;
            /** Separator of name and type. */
            public final Lexeme.Token.Colon separator;
            /** Type expression. */
            public final Expression type;
            /** Assignment operator. */
            public final Lexeme.Token.Equal assignment;
            /** Value expression. */
            public final Expression value;
            /** End of the statement. */
            public final Lexeme.Token.Semicolon end;

            public Definition(Lexeme.Identifier name, Lexeme.Token.Colon separator, Expression type,
                    Lexeme.Token.Equal assignment, Expression value,
                    Lexeme.Token.Semicolon end) {
                this.name = name;
                this.separator = separator;
                this.type = type;
                this.assignment = assignment;
                this.value = value;
                this.end = end;
            }

            @Override
            public String toString() {
                return name.toString() + separator + type + assignment + value + end;
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
            extends Node permits Expression.Primary, Expression.Group, Expression.Unary, Expression.Binary {
        /** Independent expression. */
        public static sealed abstract class Primary extends Expression permits Primary.Literal, Primary.Access {
            /** Hard coded value. */
            public static final class Literal extends Primary {
                /** Lexeme that carries the value. */
                public final Lexeme value;

                public Literal(Lexeme value) {
                    this.value = value;
                }

                @Override
                public String toString() {
                    return "[" + value + "]";
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
                public final Lexeme.Identifier name;

                public Access(Lexeme.Identifier name) {
                    this.name = name;
                }

                @Override
                public String toString() {
                    return "[" + name + "]";
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

        /** Grouping expression to elevate its position in associative ordering. */
        public static final class Group extends Expression {
            /** Expression that is elevated. */
            public final Expression elevated;
            /** Start of the group. */
            public final Lexeme.Token.OpeningParentheses opening;
            /** End of the group. */
            public final Lexeme.Token.ClosingParentheses closing;

            public Group(Expression elevated, OpeningParentheses opening, ClosingParentheses closing) {
                this.elevated = elevated;
                this.opening = opening;
                this.closing = closing;
            }

            @Override
            public String toString() {
                return "[" + opening + elevated + closing + "]";
            }

            @Override
            public int hashCode() {
                return Objects.hash(closing, elevated, opening);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Group)) {
                    return false;
                }
                Group other = (Group) obj;
                return Objects.equals(closing, other.closing) && Objects.equals(elevated, other.elevated)
                        && Objects.equals(opening, other.opening);
            }
        }

        /** Operation on an expression. */
        public static final class Unary extends Expression {
            /** Operator. */
            public final Lexeme.Token operator;
            /** Operand. */
            public final Expression operand;

            public Unary(Lexeme.Token operator, Expression operand) {
                this.operator = operator;
                this.operand = operand;
            }

            @Override
            public String toString() {
                return "[" + operator + operand + "]";
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
            /** Operator. */
            public final Lexeme.Token operator;
            /** Left operand. */
            public final Expression left;
            /** Right operand. */
            public final Expression right;

            public Binary(Lexeme.Token operator, Expression left, Expression right) {
                this.operator = operator;
                this.left = left;
                this.right = right;
            }

            @Override
            public String toString() {
                return "[" + left + operator + right + "]";
            }

            @Override
            public int hashCode() {
                return Objects.hash(left, operator, right);
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
                return Objects.equals(left, other.left) && Objects.equals(operator, other.operator)
                        && Objects.equals(right, other.right);
            }
        }
    }
}