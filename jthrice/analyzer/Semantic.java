// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

/** Semantic object in a Thrice program. */
public sealed abstract class Semantic permits Semantic.Program, Semantic.Statement, Semantic.Variable, Semantic.Expression {

}
