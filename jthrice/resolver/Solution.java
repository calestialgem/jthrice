package jthrice.resolver;

import jthrice.lexer.*;
import jthrice.parser.*;
import jthrice.parser.Node.*;
import jthrice.utility.*;

public class Solution {
  public final Node.Program                                      node;
  public final FixedMap<String, Type>                            types;
  public final FixedMap<Class<? extends Lexeme.Token>, Operator> operators;
  public final FixedMap<Lexeme.Identifier, Type>                 variables;

  public Solution(Program node, FixedMap<String, Type> types,
    FixedMap<Class<? extends Lexeme.Token>, Operator> operators,
    FixedMap<Lexeme.Identifier, Type> variables) {
    this.node      = node;
    this.types     = types;
    this.operators = operators;
    this.variables = variables;
  }
}
