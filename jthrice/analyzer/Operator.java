// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.HashMap;
import java.util.stream.Stream;

import jthrice.lexer.Lexeme;
import jthrice.utility.Map;

/** Things that operate on expressions. */
public sealed abstract class Operator permits Operator.Prefix, Operator.Postfix, Operator.Infix {
  public static final Map<Class<? extends Lexeme.Token>, Operator> OPERATORS;

  static {
    var operators = new HashMap<Class<? extends Lexeme.Token>, Operator>();
    Stream.of(Lexeme.Plus.class, Lexeme.Minus.class).forEach(token -> {
      Stream
        .of(Type.I1, Type.I2, Type.I4, Type.I8, Type.IX, Type.U1, Type.U2,
          Type.U4, Type.U8, Type.UX, Type.F4, Type.F8, Type.RINF)
        .forEach(type -> operators.put(token, new Prefix(token, type)));
    });
    Stream.of(Lexeme.Plus.class, Lexeme.Minus.class, Lexeme.Star.class,
      Lexeme.ForwardSlash.class, Lexeme.Percent.class).forEach(token -> {
        Stream
          .of(Type.I1, Type.I2, Type.I4, Type.I8, Type.IX, Type.U1, Type.U2,
            Type.U4, Type.U8, Type.UX, Type.F4, Type.F8, Type.RINF)
          .forEach(type -> operators.put(token, new Infix(token, type, type)));
      });
    OPERATORS = new Map<>(operators);
  }

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
