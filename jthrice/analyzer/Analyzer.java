// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.analyzer.Entity.Definition;
import jthrice.launcher.Resolution;
import jthrice.lexer.Lexeme;
import jthrice.parser.Node;
import jthrice.parser.Parser;
import jthrice.resolver.Resolver;
import jthrice.resolver.Solution;
import jthrice.resolver.Type;
import jthrice.utility.Bug;
import jthrice.utility.List;
import jthrice.utility.Result;

/** Creates a program entity from a program node. */
public class Analyzer {
  /** Analyze the source in the given resolution. */
  public static Result<Entity.Program> analyze(Resolution resolution) {
    var solution = Resolver.resolve(resolution);
    if (solution.empty()) {
      return Result.ofUnexisting();
    }
    var analyzer = new Analyzer(resolution, solution.get());
    return analyzer.analyze();
  }

  /** Resolution of the analyzed program node. */
  private final Resolution resolution;
  /** Analyzed program node. */
  private final Solution   solution;

  public Analyzer(Resolution resolution, Solution solution) {
    this.resolution = resolution;
    this.solution   = solution;
  }

  /** Analyze the program node. */
  private Result<Entity.Program> analyze() {
    var statements = List
      .of(this.solution.node.statements.stream().map(this::analyzeStatement)
        .filter(Result::valid).map(Result::get).toList());
    if (statements.size() < this.solution.node.statements.size()) {
      return Result.ofInvalid();
    }
    return Result.of(new Entity.Program(statements));
  }

  /** Analyze the given statement node. */
  private Result<Entity.Statement> analyzeStatement(Node.Statement statement) {
    return switch (statement) {
      case Node.Definition definition -> analyzeDefinition(definition);
    };
  }

  /** Analyze the given definition node. */
  private Result<Entity.Statement>
    analyzeDefinition(Node.Definition definition) {
    var type       = this.solution.types.at(definition.name.value);
    var expression = analyzeExpression(definition.value);
    if (expression.empty()) {
      return Result.ofInvalid();
    }
    // TODO: Type checking!
    return Result
      .of(new Entity.Definition(definition.name, type, expression.get()));
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

  /** Analyze the given unary node. */
  private Result<Entity.Expression> analyzeUnary(Node.Unary unary) {
    return Result.ofUnexisting();
  }

  /** Analyze the given binary node. */
  private Result<Entity.Expression> analyzeBinary(Node.Binary binary) {
    return Result.ofUnexisting();
  }
}
