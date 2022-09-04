// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.*;
import java.nio.file.*;

import jthrice.generator.*;

/** Launches the compiler. */
public class Launcher {
  public static void main(String[] arguments) {
    System.out.println("Thrice Java Compiler v.0.0.1");
    System.out.print("Java Version: ");
    System.out.println(System.getProperty("java.version"));
    System.out.println("Running with arguments:");
    for (var i = 0; i < arguments.length; i++) {
      System.out.printf("[%d] %s%n", i, arguments[i]);
    }
    System.out.println();

    if (arguments.length < 1) {
      System.out.println("Provide a Thrice file!");
    }
    var build = Path.of("build");
    for (var argument : arguments) {
      Resolution resolution = null;
      try {
        resolution = new Resolution(new Source(argument));
      } catch (IOException e) {
        System.out.println("Could not read the file!");
        e.printStackTrace();
        continue;
      }
      Generator.generate(resolution, build);
      if (resolution.errors() > 0) {
        System.out.printf("There were %d errors in %s!%n", resolution.errors(),
          argument);
      }
      if (resolution.warnings() > 0) {
        System.out.printf("There were %d warnings in %s!%n",
          resolution.warnings(), argument);
      }
    }
  }
}
