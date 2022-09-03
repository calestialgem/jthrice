// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

/** Things that operate on expressions. */
public sealed abstract class Operator permits Operator.Prefix, Operator.Postfix, Operator.Infix {
  /** Operator that comes before an operand. */
  public static final class Prefix extends Operator {
    /** Type of the operand. */
    public final Type operand;

    public Prefix(String lexeme, Type operand) {
      super(lexeme);
      this.operand = operand;
    }
  }

  /** Operator that comes after an operand. */
  public static final class Postfix extends Operator {
    /** Type of the operand. */
    public final Type operand;

    public Postfix(String lexeme, Type operand) {
      super(lexeme);
      this.operand = operand;
    }
  }

  /** Operator that comes between two operands. */
  public static final class Infix extends Operator {
    /** Type of the left operand. */
    public final Type left;
    /** Type of the right operand. */
    public final Type right;

    public Infix(String lexeme, Type left, Type right) {
      super(lexeme);
      this.left  = left;
      this.right = right;
    }
  }

  /** Lexeme of the operator. */
  public final String lexeme;

  public Operator(String lexeme) {
    this.lexeme = lexeme;
  }
}
