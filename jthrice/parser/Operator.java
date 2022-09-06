// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.*;

import jthrice.lexer.*;

/** Constructs of lexemes that represent an operation. */
public sealed abstract class Operator permits Operator.Nofix, Operator.Prefix, Operator.Postfix, Operator.Infix, Operator.Outfix, Operator.Knitfix {
  /** Hardcoded type or number. */
  public static final Nofix  LITERAL  = new Nofix(
    List.of(Lexeme.I1.class, Lexeme.I2.class, Lexeme.I4.class, Lexeme.I8.class,
      Lexeme.Ix.class, Lexeme.U1.class, Lexeme.U2.class, Lexeme.U4.class,
      Lexeme.U8.class, Lexeme.Ux.class, Lexeme.F4.class, Lexeme.F8.class,
      Lexeme.Rinf.class, Lexeme.Type.class, Lexeme.Number.class));
  /** Access to a variables value. */
  public static final Nofix  ACCESS   = new Nofix(
    List.of(Lexeme.Identifier.class));
  /** Keeps the sign of the same. */
  public static final Prefix POSATE   = new Prefix(Lexeme.Plus.class);
  /** Inverts the sign. */
  public static final Prefix NEGATE   = new Prefix(Lexeme.Minus.class);
  /** Adds. */
  public static final Infix  ADD      = new Infix(Lexeme.Plus.class);
  /** Subtracts. */
  public static final Infix  SUBTRACT = new Infix(Lexeme.Minus.class);
  /** Multiplies. */
  public static final Infix  MULTIPLY = new Infix(Lexeme.Star.class);
  /** Divides. */
  public static final Infix  DIVIDE   = new Infix(Lexeme.ForwardSlash.class);
  /** Takes the reminder after division. */
  public static final Infix  REMINDER = new Infix(Lexeme.Percent.class);
  /** Groups. */
  public static final Outfix GROUP    = new Outfix(
    Lexeme.OpeningParentheses.class, Lexeme.ClosingParentheses.class);

  /** Sources of value for the calculations. */
  public static final Precedence PRIMARY = new Precedence(
    List.of(LITERAL, ACCESS, GROUP), false);
  /** Right associative unary operators. */
  public static final Precedence UNARY   = new Precedence(
    List.of(POSATE, NEGATE), true);
  /** Binary operations with factors. */
  public static final Precedence FACTOR  = new Precedence(
    List.of(MULTIPLY, DIVIDE, REMINDER), false);
  /** Binary operations with terms. */
  public static final Precedence TERM    = new Precedence(
    List.of(ADD, SUBTRACT), false);

  /** Order of operators. */
  public static final Order ORDER = new Order(
    List.of(PRIMARY, UNARY, FACTOR, TERM));

  /** Operators with equal precedence. */
  public static final class Precedence {
    /** Operators in the group. */
    public final List<Operator> members;
    /** Order of the operators that are mixed. False means operators on the left
     * come first. True means operators on the right come first. */
    public final boolean        associativity;

    /** Constructor. */
    private Precedence(List<Operator> members, boolean associativity) {
      this.members       = members;
      this.associativity = associativity;
    }
  }

  /** Order of operator precedence. */
  public static final class Order {
    /** Precedence levels. */
    public final List<Precedence> levels;

    /** Constructor. */
    private Order(List<Precedence> levels) {
      this.levels = levels;
    }
  }

  /** Operator with just the operand. */
  public static final class Nofix extends Operator {
    /** List allowable of lexeme types of the operand. */
    public final List<Class<? extends Lexeme>> operands;

    /** Constructor. */
    private Nofix(List<Class<? extends Lexeme>> operands) {
      this.operands = operands;
    }
  }

  /** Operator before an operand. */
  public static final class Prefix extends Operator {
    /** Lexeme type that comes before the operand. */
    public final Class<? extends Lexeme> before;

    /** Constructor. */
    private Prefix(Class<? extends Lexeme> before) {
      this.before = before;
    }
  }

  /** Operator after an operand. */
  public static final class Postfix extends Operator {
    /** Lexeme type that comes after the operand. */
    public final Class<? extends Lexeme> after;

    /** Constructor. */
    private Postfix(Class<? extends Lexeme> after) {
      this.after = after;
    }
  }

  /** Operator between two operands. */
  public static final class Infix extends Operator {
    /** Lexeme type that comes between the operands. */
    public final Class<? extends Lexeme> between;

    /** Constructor. */
    private Infix(Class<? extends Lexeme> between) {
      this.between = between;
    }
  }

  /** Operator before and after an operand. */
  public static final class Outfix extends Operator {
    /** Lexeme type that comes before the operand. */
    public final Class<? extends Lexeme> before;
    /** Lexeme type that comes after the operand. */
    public final Class<? extends Lexeme> after;

    /** Constructor. */
    private Outfix(Class<? extends Lexeme> before,
      Class<? extends Lexeme> after) {
      this.before = before;
      this.after  = after;
    }
  }

  /** Operator interleaved with operands. */
  public static final class Knitfix extends Operator {
    /** Lexeme type that comes after the first operand. */
    public final Class<? extends Lexeme> before;
    /** Lexeme type that comes after the middle operands. */
    public final Class<? extends Lexeme> between;
    /** Lexeme type that comes after the last operand. */
    public final Class<? extends Lexeme> after;

    /** Constructor. */
    private Knitfix(Class<? extends Lexeme> before,
      Class<? extends Lexeme> between, Class<? extends Lexeme> after) {
      this.before  = before;
      this.between = between;
      this.after   = after;
    }
  }
}
