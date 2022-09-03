// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.launcher.Resolution;
import jthrice.lexer.Lexeme;
import jthrice.parser.Node;
import jthrice.parser.Parser;
import jthrice.utility.Bug;
import jthrice.utility.List;
import jthrice.utility.Result;

import jthrice.analyzer.Entity.Expression.Primary.Literal;

/** Creates a program entity from a program node. */
public class Analyzer {
  /** Analyze the source in the given resolution. */
  public static Result<Entity.Program> analyze(Resolution resolution) {
    var node = Parser.parse(resolution);
    if (node.empty()) {
      return Result.ofUnexisting();
    }
    var analyzer = new Analyzer(resolution, node.get());
    return analyzer.analyze();
  }

  /** Resolution of the analyzed program node. */
  private final Resolution   resolution;
  /** Analyzed program node. */
  private final Node.Program node;

  public Analyzer(Resolution resolution, Node.Program node) {
    this.resolution = resolution;
    this.node       = node;
  }

  private Result<Entity.Program> analyze() {
    var statements = List
      .of(this.node.statements.stream().map(this::analyzeStatement)
        .filter(Result::valid).map(Result::get).toList());
    if (statements.size() < this.node.statements.size()) {
      return Result.ofInvalid();
    }
    return Result.of(new Entity.Program(statements));
  }

  private Result<Entity.Statement> analyzeStatement(Node.Statement statement) {
    return Result.ofUnexisting();
  }

  private Result<Entity.Expression>
    analyzeExpression(Node.Expression expression) {
    return switch (expression) {
      case Node.Expression.Primary primary -> analyzePrimary(primary);
      case Node.Expression.Group group -> analyzeGroup(group);
      case Node.Expression.Unary unary -> analyzeUnary(unary);
      case Node.Expression.Binary binary -> analyzeBinary(binary);
    };
  }

  private Result<Entity.Expression>
    analyzePrimary(Node.Expression.Primary primary) {
    return switch (primary) {
      case Node.Expression.Primary.Literal literal -> analyzeLiteral(literal);
      case Node.Expression.Primary.Access access -> analyzeAccess(access);
    };
  }

  private Entity.Expression
    analyzeLiteral(Node.Expression.Primary.Literal literal) {
    return switch (literal.value) {
      case Lexeme.Number number -> switch (number) {
        case Lexeme.Integer integer ->
          new Literal(Type.I8, integer.value.longValue());
        case Lexeme.Real real -> new Literal(Type.I8, real.value.doubleValue());
      };
      case Lexeme.Keyword keyword -> switch (keyword) {
        case Lexeme.I1 i1 -> new Literal(Type.META, Type.I1);
        case Lexeme.I2 i2 -> new Literal(Type.META, Type.I2);
        case Lexeme.I4 i4 -> new Literal(Type.META, Type.I4);
        case Lexeme.I8 i8 -> new Literal(Type.META, Type.I8);
        case Lexeme.IX ix -> new Literal(Type.META, Type.IX);
        case Lexeme.U1 u1 -> new Literal(Type.META, Type.U1);
        case Lexeme.U2 u2 -> new Literal(Type.META, Type.U2);
        case Lexeme.U4 u4 -> new Literal(Type.META, Type.U4);
        case Lexeme.U8 u8 -> new Literal(Type.META, Type.U8);
        case Lexeme.UX ux -> new Literal(Type.META, Type.UX);
        case Lexeme.F4 f4 -> new Literal(Type.META, Type.F4);
        case Lexeme.F8 f8 -> new Literal(Type.META, Type.F8);
      };
      default -> throw new Bug("Literal value is invalid!");
    };
  }
}
