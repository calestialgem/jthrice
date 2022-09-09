// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import jthrice.lexer.*;

public final class Resolution {
  public static Resolution of(String name) {
    return new Resolution(name, 0, 0);
  }

  public final String name;
  private int         errors;
  private int         warnings;

  private Resolution(String name, int errors, int warnings) {
    this.name     = name;
    this.errors   = errors;
    this.warnings = warnings;
  }

  private void log(String author, String severity, String message) {
    System.out.printf("[%s] %s: %s: %s%n", author, this.name, severity,
      message);
  }

  public void error(String author, String message) {
    this.log(author, "error", message);
    this.errors++;
  }

  public void warning(String author, String message) {
    this.log(author, "warning", message);
    this.warnings++;
  }

  public void info(String author, String message) {
    this.log(author, "info", message);
  }

  public void log(String author, Portion portion, String severity,
    String message) {
    System.out.printf("[%s] %s:%d:%d:%d:%d: %s: %s%n", author, this.name,
      portion.first.line, portion.first.column, portion.last.line,
      portion.last.column, severity, message);
    portion.underline(System.out);
  }

  public void error(String author, Portion portion, String message) {
    this.log(author, portion, "error", message);
    this.errors++;
  }

  public void warning(String author, Portion portion, String message) {
    this.log(author, portion, "warning", message);
    this.warnings++;
  }

  public void info(String author, Portion portion, String message) {
    this.log(author, portion, "info", message);
  }

  void report() {
    if (this.errors > 0) {
      this.info("LAUNCHER", "There were %d errors!".formatted(this.errors));
    }
    if (this.warnings > 0) {
      this.info("LAUNCHER", "There were %d warnings!".formatted(this.warnings));
    }
  }
}
