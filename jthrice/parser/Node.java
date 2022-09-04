// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import jthrice.lexer.*;
import jthrice.utility.*;

/** Hierarchical, context-free, collection of lexemes. */
public sealed abstract class Node permits Node.Program, Node.Statement, Node.Expression {
  /** Root node, which represent the whole program. */
  public static final class Program extends Node {
    /** Statements in the program. */
    public final FixedList<Statement> statements;
    /** End of the file. */
    public final Lexeme.EOF           eof;

    public Program(FixedList<Statement> statements, Lexeme.EOF eof) {
      super(Portion.of(statements.at(0).portion, statements.atEnd(0).portion));
      this.statements = statements;
      this.eof        = eof;
    }
  }

  /** Directives that are executed by the computer in order. */
  public static sealed abstract class Statement
    extends Node permits Definition {
    public Statement(Portion portion) {
      super(portion);
    }
  }

  /** Creation of a variable. */
  public static final class Definition extends Statement {
    /** Name of the defined variable. */
    public final Lexeme.Identifier name;
    /** Separator of name and type. */
    public final Lexeme.Colon      separator;
    /** Type expression. */
    public final Expression        type;
    /** Assignment operator. */
    public final Lexeme.Equal      assignment;
    /** Value expression. */
    public final Expression        value;
    /** End of the statement. */
    public final Lexeme.Semicolon  end;

    public Definition(Lexeme.Identifier name, Lexeme.Colon separator,
      Expression type, Lexeme.Equal assignment, Expression value,
      Lexeme.Semicolon end) {
      super(Portion.of(name.portion, end.portion));
      this.name       = name;
      this.separator  = separator;
      this.type       = type;
      this.assignment = assignment;
      this.value      = value;
      this.end        = end;
    }
  }

  /** Calculations and actions that lead to a value. */
  public static sealed abstract class Expression
    extends Node permits Primary, Group, Unary, Binary {
    public Expression(Portion portion) {
      super(portion);
    }
  }

  /** Independent expression. */
  public static sealed abstract class Primary
    extends Expression permits Literal, Access {
    public Primary(Portion portion) {
      super(portion);
    }
  }

  /** Hard coded value. */
  public static final class Literal extends Primary {
    /** Lexeme that carries the value. */
    public final Lexeme value;

    public Literal(Lexeme value) {
      super(value.portion);
      this.value = value;
    }
  }

  /** Value of a variable. */
  public static final class Access extends Primary {
    /** Name of the accessed variable. */
    public final Lexeme.Identifier name;

    public Access(Lexeme.Identifier name) {
      super(name.portion);
      this.name = name;
    }
  }

  /**
   * Grouping expression to elevate its position in associative ordering.
   */
  public static final class Group extends Expression {
    /** Expression that is elevated. */
    public final Expression                elevated;
    /** Start of the group. */
    public final Lexeme.OpeningParentheses opening;
    /** End of the group. */
    public final Lexeme.ClosingParentheses closing;

    public Group(Expression elevated, Lexeme.OpeningParentheses opening,
      Lexeme.ClosingParentheses closing) {
      super(Portion.of(opening.portion, closing.portion));
      this.elevated = elevated;
      this.opening  = opening;
      this.closing  = closing;
    }
  }

  /** Operation on an expression. */
  public static final class Unary extends Expression {
    /** Operator. */
    public final Lexeme.Token operator;
    /** Operand. */
    public final Expression   operand;

    public Unary(Lexeme.Token operator, Expression operand) {
      super(Portion.of(operator.portion, operand.portion));
      this.operator = operator;
      this.operand  = operand;
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
      super(Portion.of(left.portion, right.portion));
      this.operator = operator;
      this.left     = left;
      this.right    = right;
    }
  }

  /** Portion of the node in the source file. */
  public final Portion portion;

  public Node(Portion portion) {
    this.portion = portion;
  }

  @Override
  public String toString() {
    return this.portion.toString();
  }
}
