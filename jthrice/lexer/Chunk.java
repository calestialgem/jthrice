package jthrice.lexer;

import jthrice.launcher.*;

/** Chunk of a source that grows. */
class Chunk {
  /** Chunk starting at the given index in the given source. */
  static Chunk of(Source source, int index) {
    return new Chunk(source, index, index + 1);
  }

  /** Source the chunk is in. */
  private final Source source;
  /** Starting index. */
  private final int    first;
  /** Ending index. */
  private int          last;

  /** Constructor. */
  private Chunk(Source source, int first, int last) {
    this.source = source;
    this.first  = first;
    this.last   = last;
  }

  /** Whether there is a character left in the source. */
  boolean has() {
    return this.source.exists(this.last);
  }

  /** Next character. */
  char next() {
    return this.source.at(this.last);
  }

  /** Take the next character. */
  Chunk consume() {
    this.last++;
    return this;
  }

  /** Create the portion. Returns null if it was not grown at least once. */
  Portion get() {
    return Portion.of(this.source, this.first, this.last);
  }
}
