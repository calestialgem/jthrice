package jthrice.analyzer;

public final class Group extends Evaluation {
  static Group of(Evaluation operand) {
    return new Group(operand.type, operand);
  }

  public final Evaluation operand;

  private Group(Type type, Evaluation operand) {
    super(type);
    this.operand = operand;
  }
}
