// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

public sealed abstract class Evaluation permits Literal, Access, Group, Posate, Negate, Multiply, Divide, Reminder, Add, Subtract {
  public final Type type;

  protected Evaluation(Type type) {
    this.type = type;
  }
}
