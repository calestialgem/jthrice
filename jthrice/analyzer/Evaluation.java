// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

import jthrice.lexer.*;
import jthrice.parser.*;

public sealed abstract class Evaluation permits Evaluation.Nofix, Evaluation.Prefix, Evaluation.Postfix, Evaluation.Infix, Evaluation.Outfix, Evaluation.Knitfix {
  static Nofix ofNofix(Type type, Object value, Operator operator,
    Lexeme first) {
    return new Nofix(type, value, operator, first);
  }

  static Prefix ofPrefix(Type type, Object value, Operator operator,
    Lexeme before, Evaluation last) {
    return new Prefix(type, value, operator, before, last);
  }

  static Postfix ofPostfix(Type type, Object value, Operator operator,
    Lexeme after, Evaluation first) {
    return new Postfix(type, value, operator, after, first);
  }

  static Infix ofInfix(Type type, Object value, Operator operator,
    Lexeme between, Evaluation first, Evaluation last) {
    return new Infix(type, value, operator, between, first, last);
  }

  static Outfix ofOutfix(Type type, Object value, Operator operator,
    Lexeme before, Lexeme after, Evaluation middle) {
    return new Outfix(type, value, operator, before, after, middle);
  }

  static Knitfix ofKnitfix(Type type, Object value, Operator operator,
    Lexeme before, List<Lexeme> between, Lexeme after, Evaluation first,
    List<Evaluation> middle, Evaluation last) {
    return new Knitfix(type, value, operator, before, between, after, first,
      middle, last);
  }

  public static final class Nofix extends Evaluation {
    public final Lexeme first;

    private Nofix(Type type, Object value, Operator operator, Lexeme first) {
      super(type, value, operator);
      this.first = first;
    }
  }

  public static final class Prefix extends Evaluation {
    public final Lexeme     before;
    public final Evaluation last;

    private Prefix(Type type, Object value, Operator operator, Lexeme before,
      Evaluation last) {
      super(type, value, operator);
      this.before = before;
      this.last   = last;
    }
  }

  public static final class Postfix extends Evaluation {
    public final Lexeme     after;
    public final Evaluation first;

    private Postfix(Type type, Object value, Operator operator, Lexeme after,
      Evaluation first) {
      super(type, value, operator);
      this.after = after;
      this.first = first;
    }
  }

  public static final class Infix extends Evaluation {
    public final Lexeme     between;
    public final Evaluation first;
    public final Evaluation last;

    private Infix(Type type, Object value, Operator operator, Lexeme between,
      Evaluation first, Evaluation last) {
      super(type, value, operator);
      this.between = between;
      this.first   = first;
      this.last    = last;
    }
  }

  public static final class Outfix extends Evaluation {
    public final Lexeme     before;
    public final Lexeme     after;
    public final Evaluation middle;

    private Outfix(Type type, Object value, Operator operator, Lexeme before,
      Lexeme after, Evaluation middle) {
      super(type, value, operator);
      this.before = before;
      this.after  = after;
      this.middle = middle;
    }
  }

  public static final class Knitfix extends Evaluation {
    public final Lexeme           before;
    public final List<Lexeme>     between;
    public final Lexeme           after;
    public final Evaluation       first;
    public final List<Evaluation> middle;
    public final Evaluation       last;

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

  public final Type     type;
  public final Object   value;
  public final Operator operator;

  private Evaluation(Type type, Object value, Operator operator) {
    this.type     = type;
    this.value    = value;
    this.operator = operator;
  }

  public boolean known() {
    return value != null;
  }
}
