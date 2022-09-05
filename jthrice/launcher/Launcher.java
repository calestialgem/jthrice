// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.util.stream.*;

import jthrice.lexer.*;

/** Launches the compiler. */
public class Launcher {
  /** Print the arguments. */
  private static void printArguments(String[] arguments) {
    System.out.println("Thrice Java Compiler v.0.0.1");
    System.out.print("Java Version: ");
    System.out.println(System.getProperty("java.version"));
    System.out.println("Running with arguments:");
    for (var i = 0; i < arguments.length; i++) {
      System.out.printf("[%d] %s%n", i, arguments[i]);
    }
    System.out.println();
  }

  /** Compile the source file in the given resolution. */
  public static void compile(Source source) {
    var resolution = Resolution.of(source.name);
    var lexemes    = Lexer.lex(resolution, source);
    resolution.report();
  }

  /** Process the source file at the given relative path. */
  public static void process(String name) {
    try {
      Launcher.compile(Source.of(name));
    } catch (Exception e) {
      System.out.printf("Could not process %s!%nError: %s%n", name,
        e.getLocalizedMessage());
    }
  }

  /** Run the compiler. */
  public static void main(String[] arguments) {
    Launcher.printArguments(arguments);
    if (arguments.length < 1) {
      System.out.println("Provide a Thrice file!");
    }
    Stream.of(arguments).parallel().forEach(Launcher::process);
  }

  /** Constructor. */
  private Launcher() {
  }
}
