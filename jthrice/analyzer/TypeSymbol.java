// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.Lexeme.*;
import jthrice.parser.*;

/** Symbol that represents a type. */
public final class TypeSymbol extends Symbol {
  /** Symbol of meta. */
  public static final TypeSymbol META = new TypeSymbol(Type.META.toString(),
    null, Evaluation.ofNofix(null, Type.META, Operator.LITERAL, null));
  /** Symbol of i1. */
  public static final TypeSymbol I1   = TypeSymbol.ofScalar(Scalar.I1);
  /** Symbol of i2. */
  public static final TypeSymbol I2   = TypeSymbol.ofScalar(Scalar.I2);
  /** Symbol of i4. */
  public static final TypeSymbol I4   = TypeSymbol.ofScalar(Scalar.I4);
  /** Symbol of i8. */
  public static final TypeSymbol I8   = TypeSymbol.ofScalar(Scalar.I8);
  /** Symbol of ix. */
  public static final TypeSymbol IX   = TypeSymbol.ofScalar(Scalar.IX);
  /** Symbol of u1. */
  public static final TypeSymbol U1   = TypeSymbol.ofScalar(Scalar.U1);
  /** Symbol of u2. */
  public static final TypeSymbol U2   = TypeSymbol.ofScalar(Scalar.U2);
  /** Symbol of u4. */
  public static final TypeSymbol U4   = TypeSymbol.ofScalar(Scalar.U4);
  /** Symbol of u8. */
  public static final TypeSymbol U8   = TypeSymbol.ofScalar(Scalar.U8);
  /** Symbol of ux. */
  public static final TypeSymbol UX   = TypeSymbol.ofScalar(Scalar.UX);
  /** Symbol of f4. */
  public static final TypeSymbol F4   = TypeSymbol.ofScalar(Scalar.F4);
  /** Symbol of f8. */
  public static final TypeSymbol F8   = TypeSymbol.ofScalar(Scalar.F8);
  /** Symbol of rinf. */
  public static final TypeSymbol RINF = TypeSymbol.ofScalar(Scalar.RINF);

  /** Symbol of the given scalar. */
  private static TypeSymbol ofScalar(Scalar scalar) {
    return new TypeSymbol(scalar.toString(), null,
      Evaluation.ofNofix(Type.META, scalar, Operator.LITERAL, null));
  }

  /** Constructor. */
  private TypeSymbol(String name, Identifier declaration,
    Evaluation evaluation) {
    super(name, declaration, evaluation);
  }
}
