package jthrice.lexer;

import java.lang.reflect.*;
import java.util.regex.*;

import jthrice.launcher.*;

final class Regular {
  static final Regular REGULARS[] = {
    of("[+-]?[0-9]+(\\.[0-9]+)?([eE][+-]?[0-9]+)?", Decimal.class),
    of("[a-zA-Z_][0-9a-zA-Z_]*", Identifier.class) };

  private static Regular of(String pattern, Class<? extends Lexeme> type) {
    return new Regular(Pattern.compile(pattern), type);
  }

  private final Pattern                 pattern;
  private final Class<? extends Lexeme> type;

  private Regular(Pattern pattern, Class<? extends Lexeme> type) {
    this.pattern = pattern;
    this.type    = type;
  }

  Matcher matcher(Source source) {
    return source.matcher(pattern);
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
