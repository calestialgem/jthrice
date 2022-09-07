// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

/** Type and, if known, compile-time value of an expression. */
public final class Evaluation {
  /** Evaluation of expression that is known at compile-time. */
  static Evaluation of(Type type, Object value) {
    return new Evaluation(type, value);
  }

  /** Evaluation of an expression that is not known at compile-time. */
  static Evaluation of(Type type) {
    return new Evaluation(type, null);
  }

  /** Type. */
  public final Type   type;
  /** Value. Null if not known at compile-time. */
  public final Object value;

  /** Constructor. */
  private Evaluation(Type type, Object value) {
    this.type  = type;
    this.value = value;
  }

  /** Whether the value is known at compile-time. */
  public boolean known() {
    return this.value != null;
  }
}
