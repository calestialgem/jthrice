// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import jthrice.lexer.*;

/** Resolution of a source file. */
public class Resolution {
  /** Clean resolution of the file with the given relative path. */
  public static Resolution of(String name) {
    return new Resolution(name, 0, 0);
  }

  /** Relative path to the file. */
  public final String name;
  /** Amount of errors that originated in the source file. */
  private int         errors;
  /** Amount of warnings that originated in the source file. */
  private int         warnings;

  /** Constructor. */
  private Resolution(String name, int errors, int warnings) {
    this.name     = name;
    this.errors   = errors;
    this.warnings = warnings;
  }

  /** Log the given message from the given author at the given log level. */
  private void log(String author, String severity, String message) {
    System.out.printf("[%s] %s: %s: %s%n", author, this.name, severity,
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
    System.out.printf("[%s] %s:%d:%d:%d:%d: %s: %s%n", author, this.name,
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

  /** Report the amount of errors and warnings to the user. */
  void report() {
    if (this.errors > 0) {
      this.info("LAUNCHER", "There were %d errors!".formatted(this.errors));
    }
    if (this.warnings > 0) {
      this.info("LAUNCHER", "There were %d warnings!".formatted(this.warnings));
    }
  }
}
