// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

final class Indentation {
  static Indentation of(int level) {
    return new Indentation(level);
  }

  final int level;

  private Indentation(int level) {
    this.level = level;
  }
}
