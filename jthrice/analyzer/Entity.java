// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.*;

import jthrice.lexer.*;
import jthrice.resolver.*;

/** An existince in a Thrice program. */
public sealed abstract class Entity permits Entity.Program, Entity.Statement, Entity.Expression {
  /** Root entity, which represents the whole program. */
  public static final class Program extends Entity {
    /** Statements in the program. */
    public final List<Statement> statements;

    /** Constructor. */
    private Program(List<Statement> statements) {
      this.statements = statements;
    }
  }

  /** Directives that are executed by the computer in order. */
  public static sealed abstract class Statement
    extends Entity permits Definition {
  }

  /** Creation of a variable. */
  public static final class Definition extends Statement {
    /** Variable name. */
    public final Lexeme.Identifier name;
    /** Varible type. */
    public final Type              type;
    /** Variable value. */
    public final Expression        value;

    /** Constructor. */
    private Definition(Lexeme.Identifier name, Type type, Expression value) {
      this.name  = name;
      this.type  = type;
      this.value = value;
    }
  }

  /** Calculations and actions that lead to a value. */
  public static sealed abstract class Expression
    extends Entity permits Literal, Access, Unary, Binary {
    /** Type of the expression. */
    public final Type type;

    /** Constructor. */
    private Expression(Type type) {
      this.type = type;
    }
  }

  /** Hard coded value. */
  public static final class Literal extends Expression {
    /** Value. */
    public final Object value;

    /** Constructor. */
    private Literal(Type type, Object value) {
      super(type);
      this.value = value;
    }
  }

  /** Value of a variable. */
  public static final class Access extends Expression {
    /** Name of the accessed variable. */
    public final Lexeme.Identifier variable;

    /** Constructor. */
    private Access(Type type, Lexeme.Identifier variable) {
      super(type);
      this.variable = variable;
    }
  }

  /** Operation on an expression. */
  public static final class Unary extends Expression {
    /** Operator. */
    public final String     operator;
    /** Operand. */
    public final Expression operand;

    /** Constructor. */
    private Unary(Type type, String operator, Expression operand) {
      super(type);
      this.operator = operator;
      this.operand  = operand;
    }
  }

  public static final class Binary extends Expression {
    /** Operator. */
    public final String     operator;
    /** Left operand. */
    public final Expression left;
    /** Right operand. */
    public final Expression right;

    /** Constructor. */
    private Binary(Type type, String operator, Expression left,
      Expression right) {
      super(type);
      this.operator = operator;
      this.left     = left;
      this.right    = right;
    }
  }
}
