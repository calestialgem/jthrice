// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.*;

import jthrice.lexer.*;

public sealed abstract class Node permits Node.Program, Node.Statement, Node.Expression {
  static Program ofProgram(List<Statement> statements, Lexeme.EOF eof) {
    if (statements.isEmpty()) {
      return new Program(eof.portion, statements, eof);
    }
    return new Program(Portion.of(statements.get(0).portion, eof.portion),
      statements, eof);
  }

  static Definition ofDefinition(Lexeme.Identifier name, Lexeme.Colon separator,
    Expression type, Lexeme.Equal assignment, Expression value,
    Lexeme.Semicolon end) {
    return new Definition(Portion.of(name.portion, end.portion), name,
      separator, type, assignment, value, end);
  }

  static Nofix ofNofix(Operator operator, Lexeme first) {
    return new Nofix(first.portion, operator, first);
  }

  static Prefix ofPrefix(Operator operator, Lexeme before, Expression last) {
    return new Prefix(Portion.of(before.portion, last.portion), operator,
      before, last);
  }

  static Postfix ofPostfix(Operator operator, Lexeme after, Expression first) {
    return new Postfix(Portion.of(first.portion, after.portion), operator,
      after, first);
  }

  static Infix ofInfix(Operator operator, Lexeme between, Expression first,
    Expression last) {
    return new Infix(Portion.of(first.portion, last.portion), operator, between,
      first, last);
  }

  static Outfix ofOutfix(Operator operator, Lexeme before, Lexeme after,
    Expression middle) {
    return new Outfix(Portion.of(before.portion, after.portion), operator,
      before, after, middle);
  }

  static Knitfix ofKnitfix(Operator operator, Lexeme before,
    List<Lexeme> between, Lexeme after, Expression first,
    List<Expression> middle, Expression last) {
    return new Knitfix(Portion.of(first.portion, after.portion), operator,
      before, between, after, first, middle, last);
  }

  public static final class Program extends Node {
    public final List<Statement> statements;
    public final Lexeme.EOF      eof;

    private Program(Portion portion, List<Statement> statements,
      Lexeme.EOF eof) {
      super(portion);
      this.statements = statements;
      this.eof        = eof;
    }

    @Override
    public String toString() {
      var buffer = new StringBuilder();
      for (var statement : this.statements) {
        buffer.append(statement);
        buffer.append(System.lineSeparator());
      }
      return buffer.toString();
    }
  }

  public static sealed abstract class Statement
    extends Node permits Definition {
    private Statement(Portion portion) {
      super(portion);
    }
  }

  public static final class Definition extends Statement {
    public final Lexeme.Identifier name;
    public final Lexeme.Colon      separator;
    public final Expression        type;
    public final Lexeme.Equal      assignment;
    public final Expression        value;
    public final Lexeme.Semicolon  end;

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

  public static sealed abstract class Expression
    extends Node permits Nofix, Prefix, Postfix, Infix, Outfix, Knitfix {
    public final Operator operator;

    private Expression(Portion portion, Operator operator) {
      super(portion);
      this.operator = operator;
    }
  }

  public static final class Nofix extends Expression {
    public final Lexeme first;

    private Nofix(Portion portion, Operator operator, Lexeme first) {
      super(portion, operator);
      this.first = first;
    }
  }

  public static final class Prefix extends Expression {
    public final Lexeme     before;
    public final Expression last;

    private Prefix(Portion portion, Operator operator, Lexeme before,
      Expression last) {
      super(portion, operator);
      this.before = before;
      this.last   = last;
    }
  }

  public static final class Postfix extends Expression {
    public final Lexeme     after;
    public final Expression first;

    private Postfix(Portion portion, Operator operator, Lexeme after,
      Expression first) {
      super(portion, operator);
      this.after = after;
      this.first = first;
    }
  }

  public static final class Infix extends Expression {
    public final Lexeme     between;
    public final Expression first;
    public final Expression last;

    private Infix(Portion portion, Operator operator, Lexeme between,
      Expression first, Expression last) {
      super(portion, operator);
      this.between = between;
      this.first   = first;
      this.last    = last;
    }
  }

  public static final class Outfix extends Expression {
    public final Lexeme     before;
    public final Lexeme     after;
    public final Expression middle;

    private Outfix(Portion portion, Operator operator, Lexeme before,
      Lexeme after, Expression middle) {
      super(portion, operator);
      this.before = before;
      this.after  = after;
      this.middle = middle;
    }
  }

  public static final class Knitfix extends Expression {
    public final Lexeme           before;
    public final List<Lexeme>     between;
    public final Lexeme           after;
    public final Expression       first;
    public final List<Expression> middle;
    public final Expression       last;

    private Knitfix(Portion portion, Operator operator, Lexeme before,
      List<Lexeme> between, Lexeme after, Expression first,
      List<Expression> middle, Expression last) {
      super(portion, operator);
      this.before  = before;
      this.between = between;
      this.after   = after;
      this.first   = first;
      this.middle  = middle;
      this.last    = last;
    }
  }

  public final Portion portion;

  Node(Portion portion) {
    this.portion = portion;
  }

  @Override
  public String toString() {
    return this.portion.toString();
  }
}
