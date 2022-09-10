package jthrice.parser;

import jthrice.launcher.*;

public sealed abstract class Statement extends Node permits Definition {
  protected Statement(Portion portion) {
    super(portion);
  }
}
