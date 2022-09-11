package jthrice.analyzer;

public final class Divide extends Evaluation {
  static Divide of(Evaluation left, Evaluation right) {
    return new Divide(left.type, left, right);
  }

  public final Evaluation left;
  public final Evaluation right;

  private Divide(Type type, Evaluation left, Evaluation right) {
    super(type);
    this.left  = left;
    this.right = right;
  }
}
