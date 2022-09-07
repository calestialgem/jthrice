// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

import jthrice.lexer.*;
import jthrice.parser.*;

/** Type and, if known, compile-time value of an expression. */
public sealed abstract class Evaluation permits Evaluation.Nofix, Evaluation.Prefix, Evaluation.Postfix, Evaluation.Infix, Evaluation.Outfix, Evaluation.Knitfix {
  /** Nofix evaluation of the given operand. */
  static Nofix ofNofix(Type type, Object value, Operator operator,
    Lexeme first) {
    return new Nofix(type, value, operator, first);
  }

  /** Prefix evaluation of the given operator and operand. */
  static Prefix ofPrefix(Type type, Object value, Operator operator,
    Lexeme before, Evaluation last) {
    return new Prefix(type, value, operator, before, last);
  }

  /** Postfix evaluation of the given operator and operand. */
  static Postfix ofPostfix(Type type, Object value, Operator operator,
    Lexeme after, Evaluation first) {
    return new Postfix(type, value, operator, after, first);
  }

  /** Infix evaluation of the given operator and operands. */
  static Infix ofInfix(Type type, Object value, Operator operator,
    Lexeme between, Evaluation first, Evaluation last) {
    return new Infix(type, value, operator, between, first, last);
  }

  /** Outfix evaluation of the given operators and operand. */
  static Outfix ofOutfix(Type type, Object value, Operator operator,
    Lexeme before, Lexeme after, Evaluation middle) {
    return new Outfix(type, value, operator, before, after, middle);
  }

  /** Knitfix evaluation of the given operators and operands. */
  static Knitfix ofKnitfix(Type type, Object value, Operator operator,
    Lexeme before, List<Lexeme> between, Lexeme after, Evaluation first,
    List<Evaluation> middle, Evaluation last) {
    return new Knitfix(type, value, operator, before, between, after, first,
      middle, last);
  }

  /** Operation without an operator. */
  public static final class Nofix extends Evaluation {
    /** Only operand. */
    public final Lexeme first;

    /** Constructor. */
    private Nofix(Type type, Object value, Operator operator, Lexeme first) {
      super(type, value, operator);
      this.first = first;
    }
  }

  /** Operation with a prefix operator. */
  public static final class Prefix extends Evaluation {
    /** Lexeme that comes before the operand. */
    public final Lexeme     before;
    /** Last operand. */
    public final Evaluation last;

    /** Constructor. */
    private Prefix(Type type, Object value, Operator operator, Lexeme before,
      Evaluation last) {
      super(type, value, operator);
      this.before = before;
      this.last   = last;
    }
  }

  /** Operation with a postfix operator. */
  public static final class Postfix extends Evaluation {
    /** Lexeme that comes after the operand. */
    public final Lexeme     after;
    /** First operand. */
    public final Evaluation first;

    /** Constructor. */
    private Postfix(Type type, Object value, Operator operator, Lexeme after,
      Evaluation first) {
      super(type, value, operator);
      this.after = after;
      this.first = first;
    }
  }

  /** Operation with a infix operator. */
  public static final class Infix extends Evaluation {
    /** Lexeme that comes between the operands. */
    public final Lexeme     between;
    /** First operand. */
    public final Evaluation first;
    /** Last operand. */
    public final Evaluation last;

    /** Constructor. */
    private Infix(Type type, Object value, Operator operator, Lexeme between,
      Evaluation first, Evaluation last) {
      super(type, value, operator);
      this.between = between;
      this.first   = first;
      this.last    = last;
    }
  }

  /** Operation with a outfix operator. */
  public static final class Outfix extends Evaluation {
    /** Lexeme that comes before the operand. */
    public final Lexeme     before;
    /** Lexeme that comes after the operand. */
    public final Lexeme     after;
    /** Middle operand. */
    public final Evaluation middle;

    /** Constructor. */
    private Outfix(Type type, Object value, Operator operator, Lexeme before,
      Lexeme after, Evaluation middle) {
      super(type, value, operator);
      this.before = before;
      this.after  = after;
      this.middle = middle;
    }
  }

  /** Operation with a knitfix operator. */
  public static final class Knitfix extends Evaluation {
    /** Lexeme that comes after the first operand. */
    public final Lexeme           before;
    /** Lexemes that come after the middle operands. */
    public final List<Lexeme>     between;
    /** Lexeme that comes after the last operand. */
    public final Lexeme           after;
    /** First operand. */
    public final Evaluation       first;
    /** Middle operands. */
    public final List<Evaluation> middle;
    /** Last operand. */
    public final Evaluation       last;

    /** Constructor. */
    private Knitfix(Type type, Object value, Operator operator, Lexeme before,
      List<Lexeme> between, Lexeme after, Evaluation first,
      List<Evaluation> middle, Evaluation last) {
      super(type, value, operator);
      this.before  = before;
      this.between = between;
      this.after   = after;
      this.first   = first;
      this.middle  = middle;
      this.last    = last;
    }
  }

  /** Type. */
  public final Type     type;
  /** Value. Null if not known at compile-time. */
  public final Object   value;
  /** Evaluated operator. */
  public final Operator operator;

  /** Constructor. */
  private Evaluation(Type type, Object value, Operator operator) {
    this.type     = type;
    this.value    = value;
    this.operator = operator;
  }

  /** Whether the value is known at compile-time. */
  public boolean known() {
    return this.value != null;
  }
}
