// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import jthrice.launcher.*;

public sealed abstract class Node permits Root, Statement, Expression {
  public final Portion portion;

  protected Node(Portion portion) {
    this.portion = portion;
  }

  @Override
  public String toString() {
    return portion.toString();
  }
}
