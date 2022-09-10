package jthrice.lexer;

import jthrice.launcher.*;

public sealed abstract class Token extends
  Lexeme permits Equal, Colon, Semicolon, OpeningParenthesis, ClosingParenthesis, Plus, Minus, Star, Slash, Percent, EOF {
  Token(Portion portion) {
    super(portion);
  }
}
