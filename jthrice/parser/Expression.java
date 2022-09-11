package jthrice.parser;

import jthrice.launcher.*;

public sealed abstract class Expression
  extends
  Node permits NullaryExpression, PrenaryExpression, PostaryExpression, CirnaryExpression, BinaryExpression, VariaryExpression {
  Expression(Portion portion) {
    super(portion);
  }
}
