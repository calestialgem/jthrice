// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.*;

import jthrice.lexer.*;

public sealed abstract class Operator permits Operator.Nofix, Operator.Prefix, Operator.Postfix, Operator.Infix, Operator.Outfix, Operator.Knitfix {
  public static final Nofix  LITERAL     = new Nofix(Lexeme.Number.class);
  public static final Nofix  ACCESS_I1   = new Nofix(Lexeme.I1.class);
  public static final Nofix  ACCESS_I2   = new Nofix(Lexeme.I2.class);
  public static final Nofix  ACCESS_I4   = new Nofix(Lexeme.I4.class);
  public static final Nofix  ACCESS_I8   = new Nofix(Lexeme.I8.class);
  public static final Nofix  ACCESS_Ix   = new Nofix(Lexeme.Ix.class);
  public static final Nofix  ACCESS_U1   = new Nofix(Lexeme.U1.class);
  public static final Nofix  ACCESS_U2   = new Nofix(Lexeme.U2.class);
  public static final Nofix  ACCESS_U4   = new Nofix(Lexeme.U4.class);
  public static final Nofix  ACCESS_U8   = new Nofix(Lexeme.U8.class);
  public static final Nofix  ACCESS_Ux   = new Nofix(Lexeme.Ux.class);
  public static final Nofix  ACCESS_F4   = new Nofix(Lexeme.F4.class);
  public static final Nofix  ACCESS_F8   = new Nofix(Lexeme.F8.class);
  public static final Nofix  ACCESS_Rinf = new Nofix(Lexeme.Rinf.class);
  public static final Nofix  ACCESS_Type = new Nofix(Lexeme.Type.class);
  public static final Nofix  ACCESS      = new Nofix(Lexeme.Identifier.class);
  public static final Prefix POSATE      = new Prefix(Lexeme.Plus.class);
  public static final Prefix NEGATE      = new Prefix(Lexeme.Minus.class);
  public static final Infix  ADD         = new Infix(Lexeme.Plus.class);
  public static final Infix  SUBTRACT    = new Infix(Lexeme.Minus.class);
  public static final Infix  MULTIPLY    = new Infix(Lexeme.Star.class);
  public static final Infix  DIVIDE      = new Infix(Lexeme.ForwardSlash.class);
  public static final Infix  REMAINDER   = new Infix(Lexeme.Percent.class);
  public static final Outfix GROUP       = new Outfix(
    Lexeme.OpeningParentheses.class, Lexeme.ClosingParentheses.class);

  public static final List<Operator> PRECEDENCE = List.of(Operator.SUBTRACT,
    Operator.ADD, Operator.REMAINDER, Operator.DIVIDE, Operator.MULTIPLY,
    Operator.NEGATE, Operator.POSATE, Operator.GROUP, Operator.ACCESS,
    Operator.ACCESS_I1, Operator.ACCESS_I2, Operator.ACCESS_I4,
    Operator.ACCESS_I8, Operator.ACCESS_Ix, Operator.ACCESS_U1,
    Operator.ACCESS_U2, Operator.ACCESS_U4, Operator.ACCESS_U8,
    Operator.ACCESS_Ux, Operator.ACCESS_F4, Operator.ACCESS_F8,
    Operator.ACCESS_Rinf, Operator.ACCESS_Type, Operator.LITERAL);

  public static final class Nofix extends Operator {
    public final Class<? extends Lexeme> operator;

    private Nofix(Class<? extends Lexeme> operands) {
      this.operator = operands;
    }

    @Override
    public String toString() {
      return Lexeme.toString(this.operator);
    }
  }

  public static final class Prefix extends Operator {
    public final Class<? extends Lexeme> before;

    private Prefix(Class<? extends Lexeme> before) {
      this.before = before;
    }

    @Override
    public String toString() {
      return "%soperand".formatted(Lexeme.toString(this.before));
    }
  }

  public static final class Postfix extends Operator {
    public final Class<? extends Lexeme> after;

    private Postfix(Class<? extends Lexeme> after) {
      this.after = after;
    }

    @Override
    public String toString() {
      return "operand%s".formatted(Lexeme.toString(this.after));
    }
  }

  public static final class Infix extends Operator {
    public final Class<? extends Lexeme> between;

    private Infix(Class<? extends Lexeme> between) {
      this.between = between;
    }

    @Override
    public String toString() {
      return "operand%soperand".formatted(Lexeme.toString(this.between));
    }
  }

  public static final class Outfix extends Operator {
    public final Class<? extends Lexeme> before;
    public final Class<? extends Lexeme> after;

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

  public static final class Knitfix extends Operator {
    public final Class<? extends Lexeme> before;
    public final Class<? extends Lexeme> between;
    public final Class<? extends Lexeme> after;

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
