// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.launcher.*;
import jthrice.parser.*;
import jthrice.resolver.*;

/** Creates a program entity from a program node and a solution. */
public class Analyzer {
  /** Analyze the given program node and the given solution and report to the
   * given resolution. */
  public static Entity.Program analyze(Resolution resolution, Solution solution,
    Node.Program node) {
    return null;
  }

  /** Try to analyze the given statement node and report to the given
   * resolution. */
  private static Entity.Statement analyzeStatement(Resolution resolution,
    Solution solution, Node.Statement node) {
    return switch (node) {
      case Node.Definition definition ->
        analyzeDefinition(resolution, solution, definition);
    };
  }

  /** Try to analyze the given definition node and report to the given
   * resolution. */
  private static Entity.Definition analyzeDefinition(Resolution resolution,
    Solution solution, Node.Definition node) {
    return null;
  }

  /** Try to analyze the given expression node and report to the given
   * resolution. */
  private static Entity.Expression analyzeExpression(Resolution resolution,
    Solution solution, Node.Expression node) {
    return switch (node) {
      case Node.Literal literal ->
        analyzeLiteral(resolution, solution, literal);
      case Node.Access access -> analyzeAccess(resolution, solution, access);
      case Node.Group group -> analyzeGroup(resolution, solution, group);
      case Node.Unary unary -> analyzeUnary(resolution, solution, unary);
      case Node.Binary binary -> analyzeBinary(resolution, solution, binary);
    };
  }

  /** Try to analyze the given literal node and report to the given
   * resolution. */
  private static Entity.Literal analyzeLiteral(Resolution resolution,
    Solution solution, Node.Literal node) {
    return null;
  }

  /** Try to analyze the given access node and report to the given
   * resolution. */
  private static Entity.Access analyzeAccess(Resolution resolution,
    Solution solution, Node.Access node) {
    return null;
  }

  /** Try to analyze the given group node and report to the given resolution. */
  private static Entity.Expression analyzeGroup(Resolution resolution,
    Solution solution, Node.Group node) {
    return analyzeExpression(resolution, solution, node.elevated);
  }

  /** Try to analyze the given unary node and report to the given resolution. */
  private static Entity.Unary analyzeUnary(Resolution resolution,
    Solution solution, Node.Unary node) {
    return null;
  }

  /** Try to analyze the given binary node and report to the given
   * resolution. */
  private static Entity.Binary analyzeBinary(Resolution resolution,
    Solution solution, Node.Binary node) {
    return null;
  }
}
