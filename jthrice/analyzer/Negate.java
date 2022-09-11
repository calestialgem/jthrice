package jthrice.analyzer;

public final class Negate extends Evaluation {
  static Negate of(Evaluation operand) {
    return new Negate(operand.type, operand);
  }

  public final Evaluation operand;

  private Negate(Type type, Evaluation operand) {
    super(type);
    this.operand = operand;
  }
}
