// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.*;

import jthrice.lexer.*;

/** Constructs of lexemes that represent an operation. */
public sealed abstract class Operator permits Operator.Nofix, Operator.Prefix, Operator.Postfix, Operator.Infix, Operator.Outfix, Operator.Knitfix {
  /** Hardcoded type or number. */
  public static final Nofix  LITERAL     = new Nofix(Lexeme.Number.class);
  /** Access to built-in `i1` type symbol's value. */
  public static final Nofix  ACCESS_I1   = new Nofix(Lexeme.I1.class);
  /** Access to built-in `i2` type symbol's value. */
  public static final Nofix  ACCESS_I2   = new Nofix(Lexeme.I2.class);
  /** Access to built-in `i4` type symbol's value. */
  public static final Nofix  ACCESS_I4   = new Nofix(Lexeme.I4.class);
  /** Access to built-in `i8` type symbol's value. */
  public static final Nofix  ACCESS_I8   = new Nofix(Lexeme.I8.class);
  /** Access to built-in `ix` type symbol's value. */
  public static final Nofix  ACCESS_Ix   = new Nofix(Lexeme.Ix.class);
  /** Access to built-in `u1` type symbol's value. */
  public static final Nofix  ACCESS_U1   = new Nofix(Lexeme.U1.class);
  /** Access to built-in `u2` type symbol's value. */
  public static final Nofix  ACCESS_U2   = new Nofix(Lexeme.U2.class);
  /** Access to built-in `u4` type symbol's value. */
  public static final Nofix  ACCESS_U4   = new Nofix(Lexeme.U4.class);
  /** Access to built-in `u8` type symbol's value. */
  public static final Nofix  ACCESS_U8   = new Nofix(Lexeme.U8.class);
  /** Access to built-in `ux` type symbol's value. */
  public static final Nofix  ACCESS_Ux   = new Nofix(Lexeme.Ux.class);
  /** Access to built-in `f4` type symbol's value. */
  public static final Nofix  ACCESS_F4   = new Nofix(Lexeme.F4.class);
  /** Access to built-in `f8` type symbol's value. */
  public static final Nofix  ACCESS_F8   = new Nofix(Lexeme.F8.class);
  /** Access to built-in `rinf` type symbol's value. */
  public static final Nofix  ACCESS_Rinf = new Nofix(Lexeme.Rinf.class);
  /** Access to built-in `type` type symbol's value. */
  public static final Nofix  ACCESS_Type = new Nofix(Lexeme.Type.class);
  /** Access to a variables value. */
  public static final Nofix  ACCESS      = new Nofix(Lexeme.Identifier.class);
  /** Keeps the sign of the same. */
  public static final Prefix POSATE      = new Prefix(Lexeme.Plus.class);
  /** Inverts the sign. */
  public static final Prefix NEGATE      = new Prefix(Lexeme.Minus.class);
  /** Adds. */
  public static final Infix  ADD         = new Infix(Lexeme.Plus.class);
  /** Subtracts. */
  public static final Infix  SUBTRACT    = new Infix(Lexeme.Minus.class);
  /** Multiplies. */
  public static final Infix  MULTIPLY    = new Infix(Lexeme.Star.class);
  /** Divides. */
  public static final Infix  DIVIDE      = new Infix(Lexeme.ForwardSlash.class);
  /** Takes the remainder after division. */
  public static final Infix  REMAINDER   = new Infix(Lexeme.Percent.class);
  /** Groups. */
  public static final Outfix GROUP       = new Outfix(
    Lexeme.OpeningParentheses.class, Lexeme.ClosingParentheses.class);

  /** Order of operators, from lowest to highest. */
  public static final List<Operator> PRECEDENCE = List.of(Operator.SUBTRACT,
    Operator.ADD, Operator.REMAINDER, Operator.DIVIDE, Operator.MULTIPLY,
    Operator.NEGATE, Operator.POSATE, Operator.GROUP, Operator.ACCESS,
    Operator.ACCESS_I1, Operator.ACCESS_I2, Operator.ACCESS_I4,
    Operator.ACCESS_I8, Operator.ACCESS_Ix, Operator.ACCESS_U1,
    Operator.ACCESS_U2, Operator.ACCESS_U4, Operator.ACCESS_U8,
    Operator.ACCESS_Ux, Operator.ACCESS_F4, Operator.ACCESS_F8,
    Operator.ACCESS_Rinf, Operator.ACCESS_Type, Operator.LITERAL);

  /** Operator without an operand. */
  public static final class Nofix extends Operator {
    /** Lexeme types of the operator. */
    public final Class<? extends Lexeme> operator;

    /** Constructor. */
    private Nofix(Class<? extends Lexeme> operands) {
      this.operator = operands;
    }

    @Override
    public String toString() {
      return Lexeme.toString(this.operator);
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

    @Override
    public String toString() {
      return "%soperand".formatted(Lexeme.toString(this.before));
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

    @Override
    public String toString() {
      return "operand%s".formatted(Lexeme.toString(this.after));
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

    @Override
    public String toString() {
      return "operand%soperand".formatted(Lexeme.toString(this.between));
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

    @Override
    public String toString() {
      return "%soperand%s".formatted(Lexeme.toString(this.before),
        Lexeme.toString(this.after));
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

    @Override
    public String toString() {
      return "operand%soperand%s operands%s operand%s".formatted(
        Lexeme.toString(this.before), Lexeme.toString(this.between),
        Lexeme.toString(this.between), Lexeme.toString(this.after));
    }
  }
}
