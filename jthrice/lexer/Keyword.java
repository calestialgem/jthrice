package jthrice.lexer;

import jthrice.launcher.*;

public sealed abstract class Keyword
  extends Lexeme permits I1, I2, I4, I8, Ix, U1, U2, U4, U8, Ux, F4, F8 {
  Keyword(Portion portion) {
    super(portion);
  }
}
