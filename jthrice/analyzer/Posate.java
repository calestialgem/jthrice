package jthrice.analyzer;

public final class Posate extends Evaluation {
  static Posate of(Evaluation operand) {
    return new Posate(operand.type, operand);
  }

  public final Evaluation operand;

  private Posate(Type type, Evaluation operand) {
    super(type);
    this.operand = operand;
  }
}
