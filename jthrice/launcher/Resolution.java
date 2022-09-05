// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.*;

import jthrice.lexer.*;

/** Resolution of a source file. */
public class Resolution {
  /** Clean resolution of the file at the given relative path, which does not
   * have the file extension. */
  public static Resolution of(String name) throws IOException {
    return new Resolution(Source.of(name), 0, 0);
  }

  /** Source file. */
  public final Source source;
  /** Amount of errors that originated in the source file. */
  private int         errors;
  /** Amount of warnings that originated in the source file. */
  private int         warnings;

  /** Constructor. */
  private Resolution(Source source, int errors, int warnings) {
    this.source   = source;
    this.errors   = errors;
    this.warnings = warnings;
  }

  /** Log the given message from the given author at the given log level. */
  private void log(String author, String severity, String message) {
    System.out.printf("[%s] %s: %s: %s%n", author, this.source.path, severity,
      message);
  }

  /** Log an error with the given message from the given author. */
  public void error(String author, String message) {
    this.log(author, "error", message);
    this.errors++;
  }

  /** Log a warning with the given message from the given author. */
  public void warning(String author, String message) {
    this.log(author, "warning", message);
    this.warnings++;
  }

  /** Log an info with the given message from the given author. */
  public void info(String author, String message) {
    this.log(author, "info", message);
  }

  /** Log the given message from the given author, for the given portion of the
   * source at the given log level. */
  public void log(String author, Portion portion, String severity,
    String message) {
    System.out.printf("[%s] %s:%d:%d:%d:%d: %s: %s%n", author, this.source.path,
      portion.first.line, portion.first.column, portion.last.line,
      portion.last.column, severity, message);
    portion.underline(System.out);
  }

  /** Log an error with the given message from the given author, for the given
   * portion of the source. */
  public void error(String author, Portion portion, String message) {
    this.log(author, portion, "error", message);
    this.errors++;
  }

  /** Log a warning with the given message from the given author, for the given
   * portion of the source. */
  public void warning(String author, Portion portion, String message) {
    this.log(author, portion, "warning", message);
    this.warnings++;
  }

  /** Log an info with the given message from the given author, for the given
   * portion of the source. */
  public void info(String author, Portion portion, String message) {
    this.log(author, portion, "info", message);
  }

  /** Amount of errors that originated in the source file. */
  public int errors() {
    return this.errors;
  }

  /** Amount of warnings that originated in the source file. */
  public int warnings() {
    return this.warnings;
  }
}
