// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.lexer.Lexeme.*;
import jthrice.parser.*;

public final class TypeSymbol extends Symbol {
  public static final TypeSymbol META = new TypeSymbol(Type.META.toString(),
    null, Evaluation.ofNofix(null, Type.META, Operator.LITERAL, null));
  public static final TypeSymbol I1   = TypeSymbol.ofScalar(Scalar.I1);
  public static final TypeSymbol I2   = TypeSymbol.ofScalar(Scalar.I2);
  public static final TypeSymbol I4   = TypeSymbol.ofScalar(Scalar.I4);
  public static final TypeSymbol I8   = TypeSymbol.ofScalar(Scalar.I8);
  public static final TypeSymbol IX   = TypeSymbol.ofScalar(Scalar.IX);
  public static final TypeSymbol U1   = TypeSymbol.ofScalar(Scalar.U1);
  public static final TypeSymbol U2   = TypeSymbol.ofScalar(Scalar.U2);
  public static final TypeSymbol U4   = TypeSymbol.ofScalar(Scalar.U4);
  public static final TypeSymbol U8   = TypeSymbol.ofScalar(Scalar.U8);
  public static final TypeSymbol UX   = TypeSymbol.ofScalar(Scalar.UX);
  public static final TypeSymbol F4   = TypeSymbol.ofScalar(Scalar.F4);
  public static final TypeSymbol F8   = TypeSymbol.ofScalar(Scalar.F8);
  public static final TypeSymbol RINF = TypeSymbol.ofScalar(Scalar.RINF);

  private static TypeSymbol ofScalar(Scalar scalar) {
    return new TypeSymbol(scalar.toString(), null,
      Evaluation.ofNofix(Type.META, scalar, Operator.LITERAL, null));
  }

  private TypeSymbol(String name, Identifier declaration,
    Evaluation evaluation) {
    super(name, declaration, evaluation);
  }
}
