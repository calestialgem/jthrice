// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.resolver;

import jthrice.lexer.*;

/** Things that operate on expressions. */
public sealed abstract class Operator permits Operator.Prefix, Operator.Postfix, Operator.Infix {
  /** Operator that comes before an operand. */
  public static final class Prefix extends Operator {
    /** Type of the operand. */
    public final Type operand;

    public Prefix(Class<? extends Lexeme.Token> token, Type operand) {
      super(token);
      this.operand = operand;
    }
  }

  /** Operator that comes after an operand. */
  public static final class Postfix extends Operator {
    /** Type of the operand. */
    public final Type operand;

    public Postfix(Class<? extends Lexeme.Token> token, Type operand) {
      super(token);
      this.operand = operand;
    }
  }

  /** Operator that comes between two operands. */
  public static final class Infix extends Operator {
    /** Type of the left operand. */
    public final Type left;
    /** Type of the right operand. */
    public final Type right;

    public Infix(Class<? extends Lexeme.Token> token, Type left, Type right) {
      super(token);
      this.left  = left;
      this.right = right;
    }
  }

  /** Token of the operator. */
  public final Class<? extends Lexeme.Token> token;

  public Operator(Class<? extends Lexeme.Token> token) {
    this.token = token;
  }
}
