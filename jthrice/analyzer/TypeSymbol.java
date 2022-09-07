// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.Lexeme.*;

/** Symbol that represents a type. */
public final class TypeSymbol extends Symbol {
  /** Symbol of meta. */
  public static final TypeSymbol META = new TypeSymbol(Type.META.toString(),
    null, Evaluation.of(null, Type.META));
  /** Symbol of i1. */
  public static final TypeSymbol I1   = ofScalar(Scalar.I1);
  /** Symbol of i2. */
  public static final TypeSymbol I2   = ofScalar(Scalar.I2);
  /** Symbol of i4. */
  public static final TypeSymbol I4   = ofScalar(Scalar.I4);
  /** Symbol of i8. */
  public static final TypeSymbol I8   = ofScalar(Scalar.I8);
  /** Symbol of ix. */
  public static final TypeSymbol IX   = ofScalar(Scalar.IX);
  /** Symbol of u1. */
  public static final TypeSymbol U1   = ofScalar(Scalar.U1);
  /** Symbol of u2. */
  public static final TypeSymbol U2   = ofScalar(Scalar.U2);
  /** Symbol of u4. */
  public static final TypeSymbol U4   = ofScalar(Scalar.U4);
  /** Symbol of u8. */
  public static final TypeSymbol U8   = ofScalar(Scalar.U8);
  /** Symbol of ux. */
  public static final TypeSymbol UX   = ofScalar(Scalar.UX);
  /** Symbol of f4. */
  public static final TypeSymbol F4   = ofScalar(Scalar.F4);
  /** Symbol of f8. */
  public static final TypeSymbol F8   = ofScalar(Scalar.F8);
  /** Symbol of rinf. */
  public static final TypeSymbol RINF = ofScalar(Scalar.RINF);

  /** Symbol of the given scalar. */
  private static TypeSymbol ofScalar(Scalar scalar) {
    return new TypeSymbol(scalar.toString(), null,
      Evaluation.of(Type.META, scalar));
  }

  /** Constructor. */
  private TypeSymbol(String name, Identifier declaration,
    Evaluation evaluation) {
    super(name, declaration, evaluation);
  }
}
