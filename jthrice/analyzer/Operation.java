// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

import jthrice.lexer.*;
import jthrice.parser.*;

/** Instantiation of an operator with the operand types and output type. */
public sealed abstract class Operation permits Operation.Nofix, Operation.Prefix, Operation.Postfix, Operation.Infix, Operation.Outfix, Operation.Knitfix {
  /** Operation of a nofix operator. */
  public static final class Nofix extends Operation {
    /** Operator. */
    public final Operator.Nofix operator;

    /** Constructor. */
    private Nofix(Type output, Operator.Nofix operator) {
      super(output);
      this.operator = operator;
    }

    @Override
    public Operator operator() {
      return operator();
    }

    @Override
    public String toString() {
      return "%s = %s".formatted(this.output, this.operator.operator);
    }
  }

  /** Operation of a prefix operator. */
  public static final class Prefix extends Operation {
    /** Operator. */
    public final Operator.Prefix operator;
    /** Type of the last operand. */
    public final Type            last;

    /** Constructor. */
    private Prefix(Type output, Operator.Prefix operator, Type last) {
      super(output);
      this.operator = operator;
      this.last     = last;
    }

    @Override
    public Operator operator() {
      return operator();
    }

    @Override
    public String toString() {
      return "%s = %s%s".formatted(this.output,
        Lexeme.toString(this.operator.before), this.last);
    }
  }

  /** Operation of a postfix operator. */
  public static final class Postfix extends Operation {
    /** Operator. */
    public final Operator.Postfix operator;
    /** Type of the last operand. */
    public final Type             first;

    /** Constructor. */
    private Postfix(Type output, Operator.Postfix operator, Type first) {
      super(output);
      this.operator = operator;
      this.first    = first;
    }

    @Override
    public Operator operator() {
      return operator();
    }

    @Override
    public String toString() {
      return "%s = %s%s".formatted(this.output, this.first,
        Lexeme.toString(this.operator.after));
    }
  }

  /** Operation of a infix operator. */
  public static final class Infix extends Operation {
    /** Operator. */
    public final Operator.Infix operator;
    /** Type of the last operand. */
    public final Type           first;
    /** Type of the last operand. */
    public final Type           last;

    /** Constructor. */
    private Infix(Type output, Operator.Infix operator, Type first, Type last) {
      super(output);
      this.operator = operator;
      this.first    = first;
      this.last     = last;
    }

    @Override
    public Operator operator() {
      return operator();
    }

    @Override
    public String toString() {
      return "%s = %s%s%s".formatted(this.output, this.first,
        Lexeme.toString(this.operator.between), this.last);
    }
  }

  /** Operation of a outfix operator. */
  public static final class Outfix extends Operation {
    /** Operator. */
    public final Operator.Outfix operator;
    /** Type of the middle operand. */
    public final Type            middle;

    /** Constructor. */
    private Outfix(Type output, Operator.Outfix operator, Type last) {
      super(output);
      this.operator = operator;
      this.middle   = last;
    }

    @Override
    public Operator operator() {
      return operator();
    }

    @Override
    public String toString() {
      return "%s = %s%s%s".formatted(this.output,
        Lexeme.toString(this.operator.before), this.middle,
        Lexeme.toString(this.operator.after));
    }
  }

  /** Operation of a knitfix operator. */
  public static final class Knitfix extends Operation {
    /** Operator. */
    public final Operator.Knitfix operator;
    /** Type of the last operand. */
    public final Type             first;
    /** Types of the remaining operands. */
    public final List<Type>       remaining;

    /** Constructor. */
    private Knitfix(Type output, Operator.Knitfix operator, Type first,
      List<Type> remaining) {
      super(output);
      this.operator  = operator;
      this.first     = first;
      this.remaining = remaining;
    }

    @Override
    public Operator operator() {
      return operator();
    }

    @Override
    public String toString() {
      var buffer = new StringBuilder();
      buffer.append(this.output).append(" = ").append(this.first)
        .append(Lexeme.toString(this.operator.before));
      for (var i = 0; i < this.remaining.size() - 1; i++) {
        buffer.append(this.remaining.get(i))
          .append(Lexeme.toString(this.operator.between)).append(' ');
      }
      if (!this.remaining.isEmpty()) {
        buffer.append(this.remaining.get(this.remaining.size() - 1));
      }
      buffer.append(Lexeme.toString(this.operator.after));
      return buffer.toString();
    }
  }

  /** Type of the result value of the operation. */
  public final Type output;

  /** Constructor. */
  private Operation(Type output) {
    this.output = output;
  }

  /** Operator of this operation. */
  public abstract Operator operator();
}
