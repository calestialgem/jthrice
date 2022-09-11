package jthrice.analyzer;

public final class Subtract extends Evaluation {
  static Subtract of(Evaluation left, Evaluation right) {
    return new Subtract(left.type, left, right);
  }

  public final Evaluation left;
  public final Evaluation right;

  private Subtract(Type type, Evaluation left, Evaluation right) {
    super(type);
    this.left  = left;
    this.right = right;
  }
}
