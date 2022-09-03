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
    public final List<Statement>  statements;
    /** End of the file. */
    public final Lexeme.Token.EOF eof;

    public Program(List<Statement> statements, Lexeme.Token.EOF eof) {
      this.statements = statements;
      this.eof        = eof;
    }

    @Override
    public String toString() {
      var builder = new StringBuilder();
      this.statements.forEach(
        statement -> builder.append(statement).append(System.lineSeparator()));
      return builder.toString();
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.eof, this.statements);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Program other)) {
        return false;
      }
      return Objects.equals(this.eof, other.eof)
        && Objects.equals(this.statements, other.statements);
    }
  }

  /** Directives that are executed by the computer in order. */
  public static sealed abstract class Statement
    extends Node permits Statement.Definition {
    /** Creation of a variable. */
    public static final class Definition extends Statement {
      /** Name of the defined variable. */
      public final Lexeme.Identifier      name;
      /** Separator of name and type. */
      public final Lexeme.Token.Colon     separator;
      /** Type expression. */
      public final Expression             type;
      /** Assignment operator. */
      public final Lexeme.Token.Equal     assignment;
      /** Value expression. */
      public final Expression             value;
      /** End of the statement. */
      public final Lexeme.Token.Semicolon end;

      public Definition(Lexeme.Identifier name, Lexeme.Token.Colon separator,
        Expression type, Lexeme.Token.Equal assignment, Expression value,
        Lexeme.Token.Semicolon end) {
        this.name       = name;
        this.separator  = separator;
        this.type       = type;
        this.assignment = assignment;
        this.value      = value;
        this.end        = end;
      }

      @Override
      public String toString() {
        return this.name.toString() + this.separator + this.type
          + this.assignment + this.value + this.end;
      }

      @Override
      public int hashCode() {
        return Objects.hash(this.assignment, this.end, this.name,
          this.separator, this.type, this.value);
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (!(obj instanceof Definition other)) {
          return false;
        }
        return Objects.equals(this.assignment, other.assignment)
          && Objects.equals(this.end, other.end)
          && Objects.equals(this.name, other.name)
          && Objects.equals(this.separator, other.separator)
          && Objects.equals(this.type, other.type)
          && Objects.equals(this.value, other.value);
      }
    }
  }

  /** Calculations and actions that lead to a value. */
  public static sealed abstract class Expression extends
    Node permits Expression.Primary, Expression.Group, Expression.Unary, Expression.Binary {
    /** Independent expression. */
    public static sealed abstract class Primary
      extends Expression permits Primary.Literal, Primary.Access {
      /** Hard coded value. */
      public static final class Literal extends Primary {
        /** Lexeme that carries the value. */
        public final Lexeme value;

        public Literal(Lexeme value) {
          this.value = value;
        }

        @Override
        public String toString() {
          return "[" + this.value + "]";
        }

        @Override
        public int hashCode() {
          return Objects.hash(this.value);
        }

        @Override
        public boolean equals(Object obj) {
          if (this == obj) {
            return true;
          }
          if (!(obj instanceof Literal other)) {
            return false;
          }
          return Objects.equals(this.value, other.value);
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
          return "[" + this.name + "]";
        }

        @Override
        public int hashCode() {
          return Objects.hash(this.name);
        }

        @Override
        public boolean equals(Object obj) {
          if (this == obj) {
            return true;
          }
          if (!(obj instanceof Access other)) {
            return false;
          }
          return Objects.equals(this.name, other.name);
        }
      }
    }

    /**
     * Grouping expression to elevate its position in associative ordering.
     */
    public static final class Group extends Expression {
      /** Expression that is elevated. */
      public final Expression                      elevated;
      /** Start of the group. */
      public final Lexeme.Token.OpeningParentheses opening;
      /** End of the group. */
      public final Lexeme.Token.ClosingParentheses closing;

      public Group(Expression elevated, OpeningParentheses opening,
        ClosingParentheses closing) {
        this.elevated = elevated;
        this.opening  = opening;
        this.closing  = closing;
      }

      @Override
      public String toString() {
        return "[" + this.opening + this.elevated + this.closing + "]";
      }

      @Override
      public int hashCode() {
        return Objects.hash(this.closing, this.elevated, this.opening);
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (!(obj instanceof Group other)) {
          return false;
        }
        return Objects.equals(this.closing, other.closing)
          && Objects.equals(this.elevated, other.elevated)
          && Objects.equals(this.opening, other.opening);
      }
    }

    /** Operation on an expression. */
    public static final class Unary extends Expression {
      /** Operator. */
      public final Lexeme.Token operator;
      /** Operand. */
      public final Expression   operand;

      public Unary(Lexeme.Token operator, Expression operand) {
        this.operator = operator;
        this.operand  = operand;
      }

      @Override
      public String toString() {
        return "[" + this.operator + this.operand + "]";
      }

      @Override
      public int hashCode() {
        return Objects.hash(this.operand, this.operator);
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (!(obj instanceof Unary other)) {
          return false;
        }
        return Objects.equals(this.operand, other.operand)
          && Objects.equals(this.operator, other.operator);
      }
    }

    /** Operation on two expressions. */
    public static final class Binary extends Expression {
      /** Operator. */
      public final Lexeme.Token operator;
      /** Left operand. */
      public final Expression   left;
      /** Right operand. */
      public final Expression   right;

      public Binary(Lexeme.Token operator, Expression left, Expression right) {
        this.operator = operator;
        this.left     = left;
        this.right    = right;
      }

      @Override
      public String toString() {
        return "[" + this.left + this.operator + this.right + "]";
      }

      @Override
      public int hashCode() {
        return Objects.hash(this.left, this.operator, this.right);
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (!(obj instanceof Binary other)) {
          return false;
        }
        return Objects.equals(this.left, other.left)
          && Objects.equals(this.operator, other.operator)
          && Objects.equals(this.right, other.right);
      }
    }
  }
}
