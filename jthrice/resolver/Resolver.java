// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Name: GPL-3.0-or-later

package jthrice.resolver;

import java.util.HashMap;
import java.util.stream.Stream;

import jthrice.launcher.Resolution;
import jthrice.lexer.Lexeme;
import jthrice.parser.Node;
import jthrice.parser.Parser;
import jthrice.utility.Map;
import jthrice.utility.Maybe;

public final class Resolver {
  public static Maybe<Solution> resolve(Resolution resolution) {
    var node = Parser.parse(resolution);
    if (node.not()) {
      return Maybe.of();
    }
    var resolver = new Resolver(resolution, node.get());
    return resolver.resolve();
  }

  private final Resolution                                       resolution;
  private final Node.Program                                     node;
  private final HashMap<String, Type>                            types;
  private final HashMap<Class<? extends Lexeme.Token>, Operator> operators;
  private final HashMap<Lexeme.Identifier, Type>                 variables;

  public Resolver(Resolution resolution, Node.Program node) {
    this.resolution = resolution;
    this.node       = node;
    this.types      = new HashMap<>();
    this.operators  = new HashMap<>();
    this.variables  = new HashMap<>();
  }

  private Maybe<Solution> resolve() {
    resolveBuiltin();
    var validStatements = this.node.statements.stream()
      .map(this::resolveStatement).count();
    if (validStatements < this.node.statements.size()) {
      return Maybe.of();
    }
    return Maybe.of(new Solution(this.node, new Map<>(this.types),
      new Map<>(this.operators), new Map<>(this.variables)));
  }

  private void resolveBuiltin() {
    var arithmetic = new HashMap<String, Type>();
    arithmetic.put("i1", new Type.I1());
    arithmetic.put("i2", new Type.I2());
    arithmetic.put("i4", new Type.I4());
    arithmetic.put("i8", new Type.I8());
    arithmetic.put("ix", new Type.IX());
    arithmetic.put("u1", new Type.U1());
    arithmetic.put("u2", new Type.U2());
    arithmetic.put("u4", new Type.U4());
    arithmetic.put("u8", new Type.U8());
    arithmetic.put("ux", new Type.UX());
    arithmetic.put("f4", new Type.F4());
    arithmetic.put("f8", new Type.F8());
    arithmetic.put("rinf", new Type.Rinf());

    arithmetic.values().stream().forEach(type -> {
      Stream.of(Lexeme.Plus.class, Lexeme.Minus.class).forEach(
        token -> this.operators.put(token, new Operator.Prefix(token, type)));
      Stream
        .of(Lexeme.Plus.class, Lexeme.Minus.class, Lexeme.Star.class,
          Lexeme.ForwardSlash.class, Lexeme.Percent.class)
        .forEach(token -> this.operators.put(token,
          new Operator.Infix(token, type, type)));
    });

    this.types.putAll(arithmetic);
  }

  private Maybe<Void> resolveStatement(Node.Statement statement) {
    return switch (statement) {
      case Node.Definition definition -> resolveDefinition(definition);
    };
  }

  private Maybe<Void> resolveDefinition(Node.Definition definition) {
    var same = this.variables.keySet().stream().filter(definition.name::equals)
      .findAny();
    if (same.isPresent()) {
      this.resolution.error("RESOLVER", definition.name.portion,
        "Another variable with the same name is already defined!");
      this.resolution.info("RESOLVER", same.get().portion,
        "Previous definition was here.");
      return Maybe.of();
    }
    var type = resolveExpression(definition.type);
    if (type.not()) {
      this.resolution.error("RESOLVER", definition.type.portion,
        "Could not resolve the type!");
      return Maybe.of();
    }
    this.variables.put(definition.name, type.get());
    return Maybe.of(null);
  }

  private Maybe<Type> resolveExpression(Node.Expression expression) {
    return switch (expression) {
      case Node.Primary primary -> resolvePrimary(primary);
      case Node.Group group -> resolveGroup(group);
      case Node.Unary unary -> resolveUnary(unary);
      case Node.Binary binary -> resolveBinary(binary);
    };
  }

  private Maybe<Type> resolvePrimary(Node.Primary primary) {
    return switch (primary) {
      case Node.Literal literal -> resolveLiteral(literal);
      case Node.Access access -> resolveAccess(access);
    };
  }

  private Maybe<Type> resolveLiteral(Node.Literal literal) {
    switch (literal.value) {
      case Lexeme.I1 i1:
        return Maybe.of(this.types.get(i1.toString()));
      case Lexeme.I2 i2:
        return Maybe.of(this.types.get(i2.toString()));
      case Lexeme.I4 i4:
        return Maybe.of(this.types.get(i4.toString()));
      case Lexeme.I8 i8:
        return Maybe.of(this.types.get(i8.toString()));
      case Lexeme.IX ix:
        return Maybe.of(this.types.get(ix.toString()));
      case Lexeme.U1 u1:
        return Maybe.of(this.types.get(u1.toString()));
      case Lexeme.U2 u2:
        return Maybe.of(this.types.get(u2.toString()));
      case Lexeme.U4 u4:
        return Maybe.of(this.types.get(u4.toString()));
      case Lexeme.U8 u8:
        return Maybe.of(this.types.get(u8.toString()));
      case Lexeme.UX ux:
        return Maybe.of(this.types.get(ux.toString()));
      case Lexeme.F4 f4:
        return Maybe.of(this.types.get(f4.toString()));
      case Lexeme.F8 f8:
        return Maybe.of(this.types.get(f8.toString()));
      default:
        this.resolution.error("RESOLVER", literal.portion, "Expected a type!");
        return Maybe.of();
    }
  }

  private Maybe<Type> resolveAccess(Node.Access access) {
    if (this.types.containsKey(access.name.value)) {
      return Maybe.of(this.types.get(access.name.value));
    }
    return Maybe.of();
  }

  private Maybe<Type> resolveGroup(Node.Group group) {
    return resolveExpression(group.elevated);
  }

  private Maybe<Type> resolveUnary(Node.Unary unary) {
    this.resolution.error("RESOLVER", unary.portion, "Expected a type!");
    return Maybe.of();
  }

  private Maybe<Type> resolveBinary(Node.Binary unary) {
    this.resolution.error("RESOLVER", unary.portion, "Expected a type!");
    return Maybe.of();
  }
}
