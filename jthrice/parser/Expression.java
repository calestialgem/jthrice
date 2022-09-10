package jthrice.parser;

import jthrice.launcher.*;

public sealed abstract class Expression
  extends Node permits Nullary, Prenary, Postary, Cirnary, Binary, Polinary {
  Expression(Portion portion) {
    super(portion);
  }
}
