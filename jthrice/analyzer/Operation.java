// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

import jthrice.lexer.*;
import jthrice.parser.*;

public sealed abstract class Operation permits Operation.Nofix, Operation.Prefix, Operation.Postfix, Operation.Infix, Operation.Outfix, Operation.Knitfix {
  public static final class Nofix extends Operation {
    public final Operator.Nofix operator;

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
      return "%s = %s".formatted(output, operator.operator);
    }
  }

  public static final class Prefix extends Operation {
    public final Operator.Prefix operator;
    public final Type            last;

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
      return "%s = %s%s".formatted(output, Lexeme.toString(operator.before),
        last);
    }
  }

  public static final class Postfix extends Operation {
    public final Operator.Postfix operator;
    public final Type             first;

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
      return "%s = %s%s".formatted(output, first,
        Lexeme.toString(operator.after));
    }
  }

  public static final class Infix extends Operation {
    public final Operator.Infix operator;
    public final Type           first;
    public final Type           last;

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
      return "%s = %s%s%s".formatted(output, first,
        Lexeme.toString(operator.between), last);
    }
  }

  public static final class Outfix extends Operation {
    public final Operator.Outfix operator;
    public final Type            middle;

    private Outfix(Type output, Operator.Outfix operator, Type last) {
      super(output);
      this.operator = operator;
      middle        = last;
    }

    @Override
    public Operator operator() {
      return operator();
    }

    @Override
    public String toString() {
      return "%s = %s%s%s".formatted(output, Lexeme.toString(operator.before),
        middle, Lexeme.toString(operator.after));
    }
  }

  public static final class Knitfix extends Operation {
    public final Operator.Knitfix operator;
    public final Type             first;
    public final List<Type>       remaining;

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
      buffer.append(output).append(" = ").append(first)
        .append(Lexeme.toString(operator.before));
      for (var i = 0; i < remaining.size() - 1; i++) {
        buffer.append(remaining.get(i))
          .append(Lexeme.toString(operator.between)).append(' ');
      }
      if (!remaining.isEmpty()) {
        buffer.append(remaining.get(remaining.size() - 1));
      }
      buffer.append(Lexeme.toString(operator.after));
      return buffer.toString();
    }
  }

  public final Type output;

  private Operation(Type output) {
    this.output = output;
  }

  public abstract Operator operator();
}
