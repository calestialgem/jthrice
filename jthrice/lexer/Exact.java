package jthrice.lexer;

import java.lang.reflect.*;

import jthrice.launcher.*;

final class Exact {
  static final Exact TOKENS[] = { of('=', Equal.class), of(':', Colon.class),
    of(';', Semicolon.class), of('(', OpeningParenthesis.class),
    of(')', ClosingParenthesis.class), of('+', Plus.class),
    of('-', Minus.class), of('*', Star.class), of('/', Slash.class),
    of('%', Percent.class), of(Source.EOF, EOF.class) };

  static final Exact KEYWORDS[] = { of("i1", I1.class), of("i2", I2.class),
    of("i4", I4.class), of("i8", I8.class), of("ix", Ix.class),
    of("u1", U1.class), of("u2", U2.class), of("u4", U4.class),
    of("u8", U8.class), of("ux", Ux.class), of("f4", F4.class),
    of("f8", F8.class), };

  private static Exact of(String lexeme, Class<? extends Lexeme> type) {
    return new Exact(lexeme, type);
  }

  private static Exact of(char lexeme, Class<? extends Lexeme> type) {
    return new Exact(String.valueOf(lexeme), type);
  }

  private final String                  lexeme;
  private final Class<? extends Lexeme> type;

  private Exact(String lexeme, Class<? extends Lexeme> type) {
    this.lexeme = lexeme;
    this.type   = type;
  }

  boolean matches(Source source, int index) {
    return source.matches(lexeme, index);
  }

  int length() {
    return lexeme.length();
  }

  Lexeme create(Portion portion) {
    try {
      return type.getDeclaredConstructor(Portion.class).newInstance(portion);
    } catch (InstantiationException | IllegalAccessException
      | IllegalArgumentException | InvocationTargetException
      | NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
      return null;
    }
  }
}
