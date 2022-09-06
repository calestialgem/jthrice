// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.*;

import jthrice.lexer.*;

/** Hierarchical, context-free, collection of lexemes. */
public sealed abstract class Node permits Node.Program, Node.Statement, Node.Expression {
  /** Program of the given statements and EOF. */
  static Program ofProgram(List<Statement> statements, Lexeme.EOF eof) {
    return new Program(Portion.of(statements.get(0).portion,
      statements.get(statements.size() - 1).portion), statements, eof);
  }

  /** Definition of the given name, separator, type, assignment, value and
   * end. */
  static Definition ofDefinition(Lexeme.Identifier name, Lexeme.Colon separator,
    Expression type, Lexeme.Equal assignment, Expression value,
    Lexeme.Semicolon end) {
    return new Definition(Portion.of(name.portion, end.portion), name,
      separator, type, assignment, value, end);
  }

  /** Nofix expression of the given operand. */
  static Nofix ofNofix(Lexeme first) {
    return new Nofix(first.portion, first);
  }

  /** Prefix expression of the given operator and operand. */
  static Prefix ofPrefix(Lexeme before, Expression last) {
    return new Prefix(Portion.of(before.portion, last.portion), before, last);
  }

  /** Postfix expression of the given operator and operand. */
  static Postfix ofPostfix(Lexeme after, Expression first) {
    return new Postfix(Portion.of(first.portion, after.portion), after, first);
  }

  /** Infix expression of the given operator and operands. */
  static Infix ofInfix(Lexeme between, Expression first, Expression last) {
    return new Infix(Portion.of(first.portion, last.portion), between, first,
      last);
  }

  /** Outfix expression of the given operators and operand. */
  static Outfix ofOutfix(Lexeme before, Lexeme after, Expression middle) {
    return new Outfix(Portion.of(before.portion, after.portion), before, after,
      middle);
  }

  /** Knitfix expression of the given operators and operands. */
  static Knitfix ofKnitfix(Lexeme before, List<Lexeme> between, Lexeme after,
    Expression first, List<Expression> middle, Expression last) {
    return new Knitfix(Portion.of(first.portion, after.portion), before,
      between, after, first, middle, last);
  }

  /** Root node, which represent the whole program. */
  public static final class Program extends Node {
    /** Statements in the program. */
    public final List<Statement> statements;
    /** End of the file. */
    public final Lexeme.EOF      eof;

    /** Constructor. */
    private Program(Portion portion, List<Statement> statements,
      Lexeme.EOF eof) {
      super(portion);
      this.statements = statements;
      this.eof        = eof;
    }
  }

  /** Directives that are executed by the computer in order. */
  public static sealed abstract class Statement
    extends Node permits Definition {
    /** Constructor. */
    private Statement(Portion portion) {
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

    /** Constructor. */
    private Definition(Portion portion, Lexeme.Identifier name,
      Lexeme.Colon separator, Expression type, Lexeme.Equal assignment,
      Expression value, Lexeme.Semicolon end) {
      super(portion);
      this.name       = name;
      this.separator  = separator;
      this.type       = type;
      this.assignment = assignment;
      this.value      = value;
      this.end        = end;
    }
  }

  /** Operations with operators that are lexemes and operands that are other
   * expressions or a lexeme. */
  public static sealed abstract class Expression
    extends Node permits Nofix, Prefix, Postfix, Infix, Outfix, Knitfix {
    /** Constructor. */
    private Expression(Portion portion) {
      super(portion);
    }
  }

  /** Operation without an operator. */
  public static final class Nofix extends Expression {
    /** Only operand. */
    public final Lexeme first;

    /** Constructor. */
    private Nofix(Portion portion, Lexeme first) {
      super(portion);
      this.first = first;
    }
  }

  /** Operation with a prefix operator. */
  public static final class Prefix extends Expression {
    /** Lexeme that comes before the operand. */
    public final Lexeme     before;
    /** Last operand. */
    public final Expression last;

    /** Constructor. */
    private Prefix(Portion portion, Lexeme before, Expression last) {
      super(portion);
      this.before = before;
      this.last   = last;
    }
  }

  /** Operation with a postfix operator. */
  public static final class Postfix extends Expression {
    /** Lexeme that comes after the operand. */
    public final Lexeme     after;
    /** First operand. */
    public final Expression first;

    /** Constructor. */
    private Postfix(Portion portion, Lexeme after, Expression first) {
      super(portion);
      this.after = after;
      this.first = first;
    }
  }

  /** Operation with a infix operator. */
  public static final class Infix extends Expression {
    /** Lexeme that comes between the operands. */
    public final Lexeme     between;
    /** First operand. */
    public final Expression first;
    /** Last operand. */
    public final Expression last;

    /** Constructor. */
    private Infix(Portion portion, Lexeme between, Expression first,
      Expression last) {
      super(portion);
      this.between = between;
      this.first   = first;
      this.last    = last;
    }
  }

  /** Operation with a outfix operator. */
  public static final class Outfix extends Expression {
    /** Lexeme that comes before the operand. */
    public final Lexeme     before;
    /** Lexeme that comes after the operand. */
    public final Lexeme     after;
    /** Middle operand. */
    public final Expression middle;

    /** Constructor. */
    private Outfix(Portion portion, Lexeme before, Lexeme after,
      Expression middle) {
      super(portion);
      this.before = before;
      this.after  = after;
      this.middle = middle;
    }
  }

  /** Operation with a knitfix operator. */
  public static final class Knitfix extends Expression {
    /** Lexeme that comes after the first operand. */
    public final Lexeme           before;
    /** Lexemes that come after the middle operands. */
    public final List<Lexeme>     between; // f(a, b, c, d)
    /** Lexeme that comes after the last operand. */
    public final Lexeme           after;
    /** First operand. */
    public final Expression       first;
    /** Middle operands. */
    public final List<Expression> middle;
    /** Last operand. */
    public final Expression       last;

    /** Constructor. */
    private Knitfix(Portion portion, Lexeme before, List<Lexeme> between,
      Lexeme after, Expression first, List<Expression> middle,
      Expression last) {
      super(portion);
      this.before  = before;
      this.between = between;
      this.after   = after;
      this.first   = first;
      this.middle  = middle;
      this.last    = last;
    }
  }

  /** Portion of the node in the source file. */
  public final Portion portion;

  /** Constructor. */
  Node(Portion portion) {
    this.portion = portion;
  }

  @Override
  public String toString() {
    return this.portion.toString();
  }
}
