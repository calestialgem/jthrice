package jthrice.analyzer;

public final class Add extends Evaluation {
  static Add of(Evaluation left, Evaluation right) {
    return new Add(left.type, left, right);
  }

  public final Evaluation left;
  public final Evaluation right;

  private Add(Type type, Evaluation left, Evaluation right) {
    super(type);
    this.left  = left;
    this.right = right;
  }
}
