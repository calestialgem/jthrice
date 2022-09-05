// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

/** Object that represents a new line with indentation. */
final class Indentation {
  /** Indentation with the given indentation level. */
  static Indentation of(int level) {
    return new Indentation(level);
  }

  /** Identation level of the new line. */
  final int level;

  /** Constructor. */
  private Indentation(int level) {
    this.level = level;
  }
}
