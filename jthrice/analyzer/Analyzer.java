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

  /** Analyze the program node. */
  private Result<Entity.Program> analyze() {
    var statements = List
      .of(this.node.statements.stream().map(this::analyzeStatement)
        .filter(Result::valid).map(Result::get).toList());
    if (statements.size() < this.node.statements.size()) {
      return Result.ofInvalid();
    }
    return Result.of(new Entity.Program(statements));
  }

  /** Analyze the given statement node. */
  private Result<Entity.Statement> analyzeStatement(Node.Statement statement) {
    return Result.ofUnexisting();
  }

  /** Analyze the given expression node. */
  private Result<Entity.Expression>
    analyzeExpression(Node.Expression expression) {
    return switch (expression) {
      case Node.Primary primary -> analyzePrimary(primary);
      case Node.Group group -> analyzeGroup(group);
      case Node.Unary unary -> analyzeUnary(unary);
      case Node.Binary binary -> analyzeBinary(binary);
    };
  }

  /** Analyze the given primary node. */
  private Result<Entity.Expression> analyzePrimary(Node.Primary primary) {
    return switch (primary) {
      case Node.Literal literal -> analyzeLiteral(literal);
      case Node.Access access -> analyzeAccess(access);
    };
  }

  /** Analyze the given literal node. */
  private static Result<Entity.Expression>
    analyzeLiteral(Node.Literal literal) {
    return Result.of(switch (literal.value) {
      case Lexeme.Number number -> new Entity.Literal(Type.RINF, number.value);
      case Lexeme.Keyword keyword -> switch (keyword) {
        case Lexeme.I1 i1 -> new Entity.Literal(Type.META, Type.I1);
        case Lexeme.I2 i2 -> new Entity.Literal(Type.META, Type.I2);
        case Lexeme.I4 i4 -> new Entity.Literal(Type.META, Type.I4);
        case Lexeme.I8 i8 -> new Entity.Literal(Type.META, Type.I8);
        case Lexeme.IX ix -> new Entity.Literal(Type.META, Type.IX);
        case Lexeme.U1 u1 -> new Entity.Literal(Type.META, Type.U1);
        case Lexeme.U2 u2 -> new Entity.Literal(Type.META, Type.U2);
        case Lexeme.U4 u4 -> new Entity.Literal(Type.META, Type.U4);
        case Lexeme.U8 u8 -> new Entity.Literal(Type.META, Type.U8);
        case Lexeme.UX ux -> new Entity.Literal(Type.META, Type.UX);
        case Lexeme.F4 f4 -> new Entity.Literal(Type.META, Type.F4);
        case Lexeme.F8 f8 -> new Entity.Literal(Type.META, Type.F8);
        case Lexeme.Rinf rinf -> new Entity.Literal(Type.META, Type.RINF);
      };
      default -> throw new Bug("Literal value is invalid!");
    });
  }

  /** Analyze the given access node. */
  private Result<Entity.Expression> analyzeAccess(Node.Access access) {
    return Result.ofUnexisting();
  }

  /** Analyze the given group node. */
  private Result<Entity.Expression> analyzeGroup(Node.Group group) {
    return analyzeExpression(group.elevated);
  }

  /** Analyze the given binary node. */
}
