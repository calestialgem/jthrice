// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.nio.file.*;
import java.util.stream.*;

import jthrice.generator.*;

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
  public static void compile(Resolution resolution) {
    Generator.generate(resolution, Path.of("build"));
    if (resolution.errors() > 0) {
      System.out.printf("There were %d errors in %s!%n", resolution.errors(),
        resolution.source.name);
    }
    if (resolution.warnings() > 0) {
      System.out.printf("There were %d warnings in %s!%n",
        resolution.warnings(), resolution.source.name);
    }
  }

  /** Run the compiler. */
  public static void main(String[] arguments) {
    printArguments(arguments);

    if (arguments.length < 1) {
      System.out.println("Provide a Thrice file!");
    }
    Stream.of(arguments).parallel()
      .forEach(argument -> Resolution.of(argument).use(Launcher::compile,
        error -> System.out.printf("Could not read file %s!%nError: %s%n",
          argument, error.getLocalizedMessage())));
  }
}
