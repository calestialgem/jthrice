// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.resolver;

import java.util.*;

import jthrice.lexer.*;

/** An existince in a Thrice program. */
public sealed abstract class Entity permits Entity.Program, Entity.Statement, Entity.Expression, Entity.Symbol {
  /** Type meta. */
  public static final Meta META = new Meta("type", null, null);
  /** Type i1. */
  public static final I1   I1   = new I1("i1", null, Entity.META);
  /** Type i2. */
  public static final I2   I2   = new I2("i2", null, Entity.META);
  /** Type i4. */
  public static final I4   I4   = new I4("i4", null, Entity.META);
  /** Type i8. */
  public static final I8   I8   = new I8("i8", null, Entity.META);
  /** Type ix. */
  public static final Ix   IX   = new Ix("ix", null, Entity.META);
  /** Type u1. */
  public static final U1   U1   = new U1("u1", null, Entity.META);
  /** Type u2. */
  public static final U2   U2   = new U2("u2", null, Entity.META);
  /** Type u4. */
  public static final U4   U4   = new U4("u4", null, Entity.META);
  /** Type u8. */
  public static final U8   U8   = new U8("u8", null, Entity.META);
  /** Type ux. */
  public static final Ux   UX   = new Ux("ux", null, Entity.META);
  /** Type f4. */
  public static final F4   F4   = new F4("f4", null, Entity.META);
  /** Type f8. */
  public static final F8   F8   = new F8("f8", null, Entity.META);
  /** Type rinf. */
  public static final Rinf RINF = new Rinf("rinf", null, Entity.META);

  /** Built-in symbols. */
  public static final Symbol[] BUILT_IN = { Entity.META, Entity.I1, Entity.I2,
    Entity.I4, Entity.I8, Entity.IX, Entity.U1, Entity.U2, Entity.U4, Entity.U8,
    Entity.UX, Entity.F4, Entity.F8, Entity.RINF };

  /** Root entity, which represents the whole program. */
  public static final class Program extends Entity {
    /** Symbols in the program. */
    public final Map<String, Symbol> symbols;
    /** Statements in the program. */
    public final List<Statement>     statements;

    /** Constructor. */
    private Program(Map<String, Symbol> symbols, List<Statement> statements) {
      this.symbols    = symbols;
      this.statements = statements;
    }
  }

  /** Directives that are executed by the computer in order. */
  public static sealed abstract class Statement
    extends Entity permits Definition {
  }

  /** Binding an expression to a symbol by the user. */
  public static final class Definition extends Statement {
    /** Defined symbol. */
    public final Symbol     symbol;
    /** Bound expression. */
    public final Expression bound;

    /** Constructor. */
    private Definition(Symbol symbol, Expression bound) {
      this.symbol = symbol;
      this.bound  = bound;
    }
  }

  /** Calculations and actions that lead to a value. */
  public static sealed abstract class Expression
    extends Entity permits Literal, Access, Unary, Binary {
    /** Type of the expression. */
    public final Type   type;
    /** Compile-time value of the expression. Null if its not known. */
    public final Object constant;

    /** Constructor. */
    private Expression(Type type, Object constant) {
      this.type     = type;
      this.constant = constant;
    }
  }

  /** Hard coded value. */
  public static final class Literal extends Expression {
    /** Constructor. */
    private Literal(Type type, Object constant) {
      super(type, constant);
    }
  }

  /** Value of a variable. */
  public static final class Access extends Expression {
    /** Accessed variable. */
    public final Variable accessed;

    /** Constructor. */
    private Access(Type type, Object constant, Variable accessed) {
      super(type, constant);
      this.accessed = accessed;
    }
  }

  /** Operation on an expression. */
  public static final class Unary extends Expression {
    /** Operator. */
    public final String     operator;
    /** Operand. */
    public final Expression operand;

    /** Constructor. */
    private Unary(Type type, Object constant, String operator,
      Expression operand) {
      super(type, constant);
      this.operator = operator;
      this.operand  = operand;
    }
  }

  /** Operation on two expressions. */
  public static final class Binary extends Expression {
    /** Operator. */
    public final String     operator;
    /** Left operand. */
    public final Expression left;
    /** Right operand. */
    public final Expression right;

    /** Constructor. */
    private Binary(Type type, Object constant, String operator, Expression left,
      Expression right) {
      super(type, constant);
      this.operator = operator;
      this.left     = left;
      this.right    = right;
    }
  }

  /** A name and a type: variables or types. Can be built-in or user-defined. */
  public static sealed abstract class Symbol
    extends Entity permits Symbol.Type, Symbol.Variable {
    /** String that matches to the symbol. */
    public final String            name;
    /** Lexeme that defines the symbol. Null if the symbol is built-in. */
    public final Lexeme.Identifier definition;
    /** Type of the symbol. Null if the type is the meta type, type of types. */
    public final Type              type;

    /** Constructor. */
    private Symbol(String name, Lexeme.Identifier definition, Type type) {
      this.name       = name;
      this.definition = definition;
      this.type       = type;
    }

    @Override
    public String toString() {
      return this.name.toString();
    }
  }

  /** Variable with the given name and type. */
  static final Variable ofVariable(String name, Lexeme.Identifier definition,
    Type type) {
    return new Variable(name, definition, type);
  }

  /** Type of a value. */
  public static sealed abstract class Type
    extends Symbol permits Type.Meta, Type.Scalar {
    /** Constructor. */
    private Type(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Meta type; type type. Type of any expression that results in a type. */
  public static final class Meta extends Type {
    /** Constructor. */
    private Meta(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Independent, undivisible, built-in types. */
  public static sealed abstract class Scalar
    extends Type permits Signed, Unsigned, Floating, Rinf {
    /** Constructor. */
    private Scalar(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Integers with a bit for signedness. */
  public static sealed abstract class Signed
    extends Scalar permits I1, I2, I4, I8, Ix {
    /** Constructor. */
    private Signed(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 1 byte, signed integer. */
  public static final class I1 extends Signed {
    /** Constructor. */
    private I1(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 2 byte, signed integer. */
  public static final class I2 extends Signed {
    /** Constructor. */
    private I2(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 4 byte, signed integer. */
  public static final class I4 extends Signed {
    /** Constructor. */
    private I4(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 8 byte, signed integer. */
  public static final class I8 extends Signed {
    /** Constructor. */
    private I8(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Pointer size, signed integer. */
  public static final class Ix extends Signed {
    /** Constructor. */
    private Ix(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Integers without a sign bit. */
  public static sealed abstract class Unsigned
    extends Scalar permits U1, U2, U4, U8, Ux {
    /** Constructor. */
    private Unsigned(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 1 byte, unsigned integer. */
  public static final class U1 extends Unsigned {
    /** Constructor. */
    private U1(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 2 byte, unsigned integer. */
  public static final class U2 extends Unsigned {
    /** Constructor. */
    private U2(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 4 byte, unsigned integer. */
  public static final class U4 extends Unsigned {
    /** Constructor. */
    private U4(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 8 byte, unsigned integer. */
  public static final class U8 extends Unsigned {
    /** Constructor. */
    private U8(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Pointer size, unsigned integer. */
  public static final class Ux extends Unsigned {
    /** Constructor. */
    private Ux(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Real numbers stored as sign, fraction and exponent such that the position
   * of the decimal point can vary. */
  public static sealed abstract class Floating extends Scalar permits F4, F8 {
    /** Constructor. */
    private Floating(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 4 byte, floating-point real. */
  public static final class F4 extends Floating {
    /** Constructor. */
    private F4(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** 8 byte, floating-point real. */
  public static final class F8 extends Floating {
    /** Constructor. */
    private F8(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Infinite-precision, compile-time real. Type of any number literal. */
  public static final class Rinf extends Scalar {
    /** Constructor. */
    private Rinf(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }

  /** Symbol of a chunk of memory. */
  public static final class Variable extends Symbol {
    /** Constructor. */
    private Variable(String name, Lexeme.Identifier definition, Type type) {
      super(name, definition, type);
    }
  }
}
