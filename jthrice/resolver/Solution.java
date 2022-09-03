package jthrice.resolver;

import jthrice.lexer.Lexeme;
import jthrice.parser.Node;
import jthrice.parser.Node.Program;
import jthrice.utility.Map;

public class Solution {
  public final Node.Program                                 node;
  public final Map<String, Type>                            types;
  public final Map<Class<? extends Lexeme.Token>, Operator> operators;
  public final Map<Lexeme.Identifier, Type>                 variables;

  public Solution(Program node, Map<String, Type> types,
    Map<Class<? extends Lexeme.Token>, Operator> operators,
    Map<Lexeme.Identifier, Type> variables) {
    this.node      = node;
    this.types     = types;
    this.operators = operators;
    this.variables = variables;
  }
}
