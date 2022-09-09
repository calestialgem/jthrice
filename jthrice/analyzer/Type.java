// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

public sealed abstract class Type permits Type.Meta, Scalar {
  public static final Meta META = new Meta();

  protected Type() {
  }

  public static final class Meta extends Type {
    private Meta() {
    }

    @Override
    public String toString() {
      return "type";
    }
  }
}
