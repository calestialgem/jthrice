package jthrice.analyzer;

public final class Reminder extends Evaluation {
  static Reminder of(Evaluation left, Evaluation right) {
    return new Reminder(left.type, left, right);
  }

  public final Evaluation left;
  public final Evaluation right;

  private Reminder(Type type, Evaluation left, Evaluation right) {
    super(type);
    this.left  = left;
    this.right = right;
  }
}
