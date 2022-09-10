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
    static Regular of(String pattern, String name) {
      return new Regular(Pattern.compile(pattern), name);
    }

    public final Pattern pattern;
    public final String  name;

    private Regular(Pattern pattern, String name) {
      this.pattern = pattern;
      this.name    = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public static final class Exact extends Type {
    static Exact of(String lexeme) {
      return new Exact(lexeme);
    }

    static Exact of(char lexeme) {
      return new Exact(String.valueOf(lexeme));
    }

    public final String lexeme;

    private Exact(String lexeme) {
      this.lexeme = lexeme;
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
    .of("[+-]?[0-9]+(\\.[0-9]+)?([eE][+-]?[0-9]+)?", "decimal");
  public static final Regular IDENTIFIER = Regular.of("[a-zA-Z_][0-9a-zA-Z_]*",
    "identifier");

  public static final Exact I1                  = Exact.of("i1");
  public static final Exact I2                  = Exact.of("i2");
  public static final Exact I4                  = Exact.of("i4");
  public static final Exact I8                  = Exact.of("i8");
  public static final Exact IX                  = Exact.of("ix");
  public static final Exact U1                  = Exact.of("u1");
  public static final Exact U2                  = Exact.of("u2");
  public static final Exact U4                  = Exact.of("u4");
  public static final Exact U8                  = Exact.of("u8");
  public static final Exact UX                  = Exact.of("ux");
  public static final Exact F4                  = Exact.of("f4");
  public static final Exact F8                  = Exact.of("f8");
  public static final Exact PLUS                = Exact.of('+');
  public static final Exact MINUS               = Exact.of('-');
  public static final Exact STAR                = Exact.of('*');
  public static final Exact FORWARD_SLASH       = Exact.of('/');
  public static final Exact PERCENT             = Exact.of('%');
  public static final Exact EQUAL               = Exact.of('=');
  public static final Exact COLON               = Exact.of(':');
  public static final Exact SEMICOLON           = Exact.of(';');
  public static final Exact OPENING_PARENTHESES = Exact.of('(');
  public static final Exact CLOSING_PARENTHESES = Exact.of(')');
  public static final Exact EOF                 = Exact.of(Source.EOF);

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
