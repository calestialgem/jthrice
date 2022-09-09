// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.*;
import java.util.regex.*;

import jthrice.launcher.*;

public final class Token {
  public static sealed abstract class Type permits Regular, Exact {
  }

  public static final class Regular extends Type {
    static Regular of(String pattern, String name, boolean separate) {
      return new Regular(Pattern.compile("^" + pattern), name, separate);
    }

    public final Pattern pattern;
    public final String  name;
    public final boolean separate;

    private Regular(Pattern pattern, String name, boolean separate) {
      this.pattern  = pattern;
      this.name     = name;
      this.separate = separate;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public static final class Exact extends Type {
    static Exact of(String lexeme, boolean separate) {
      return new Exact(lexeme, separate);
    }

    static Exact of(char lexeme, boolean separate) {
      return new Exact(String.valueOf(lexeme), separate);
    }

    public final String  lexeme;
    public final boolean separate;

    private Exact(String lexeme, boolean separate) {
      this.lexeme   = lexeme;
      this.separate = separate;
    }

    @Override
    public String toString() {
      return lexeme;
    }
  }

  static Token of(Type type, Portion portion) {
    return new Token(Optional.of(type), portion);
  }

  static Token of(Portion portion) {
    return new Token(Optional.empty(), portion);
  }

  public static final Regular DECIMAL    = Regular
    .of("[+-]?[0-9]+(\\.[0-9]+)?([eE][+-]?[0-9]+)?", "decimal", true);
  public static final Regular IDENTIFIER = Regular.of("[a-zA-Z_][0-9a-zA-Z_]+",
    "identifier", true);

  public static final Exact I1                  = Exact.of("i1", true);
  public static final Exact I2                  = Exact.of("i2", true);
  public static final Exact I4                  = Exact.of("i4", true);
  public static final Exact I8                  = Exact.of("i8", true);
  public static final Exact IX                  = Exact.of("ix", true);
  public static final Exact U1                  = Exact.of("u1", true);
  public static final Exact U2                  = Exact.of("u2", true);
  public static final Exact U4                  = Exact.of("u4", true);
  public static final Exact U8                  = Exact.of("u8", true);
  public static final Exact UX                  = Exact.of("ux", true);
  public static final Exact F4                  = Exact.of("f4", true);
  public static final Exact F8                  = Exact.of("f8", true);
  public static final Exact PLUS                = Exact.of('+', false);
  public static final Exact MINUS               = Exact.of('-', false);
  public static final Exact STAR                = Exact.of('*', false);
  public static final Exact FORWARD_SLASH       = Exact.of('/', false);
  public static final Exact PERCENT             = Exact.of('%', false);
  public static final Exact EQUAL               = Exact.of('=', false);
  public static final Exact COLON               = Exact.of(':', false);
  public static final Exact SEMICOLON           = Exact.of(';', false);
  public static final Exact OPENING_PARENTHESES = Exact.of('(', false);
  public static final Exact CLOSING_PARENTHESES = Exact.of(')', false);
  public static final Exact EOF                 = Exact.of(Source.EOF, false);

  public final Optional<Type> type;
  public final Portion        portion;

  private Token(Optional<Type> type, Portion portion) {
    this.type    = type;
    this.portion = portion;
  }

  @Override
  public String toString() {
    return portion.toString();
  }
}
