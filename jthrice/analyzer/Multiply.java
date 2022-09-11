package jthrice.analyzer;

public final class Multiply extends Evaluation {
  static Multiply of(Evaluation left, Evaluation right) {
    return new Multiply(left.type, left, right);
  }

  public final Evaluation left;
  public final Evaluation right;

  private Multiply(Type type, Evaluation left, Evaluation right) {
    super(type);
    this.left  = left;
    this.right = right;
  }
}
