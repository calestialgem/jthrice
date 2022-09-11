package jthrice.analyzer;

public final class Access extends Evaluation {
  static Access of(Evaluation accessed) {
    return new Access(accessed.type, accessed);
  }

  public final Evaluation accessed;

  private Access(Type type, Evaluation accessed) {
    super(type);
    this.accessed = accessed;
  }
}
