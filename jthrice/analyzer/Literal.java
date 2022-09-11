package jthrice.analyzer;

public final class Literal extends Evaluation {
  static Literal of(Type type, Object value) {
    return new Literal(type, value);
  }

  public final Object value;

  private Literal(Type type, Object value) {
    super(type);
    this.value = value;
  }
}
