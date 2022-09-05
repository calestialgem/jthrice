// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.nio.file.*;
import java.util.stream.*;

import jthrice.analyzer.*;
import jthrice.generator.*;
import jthrice.lexer.*;
import jthrice.parser.*;
import jthrice.resolver.*;

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

  /** Compile the given source file. */
  public static void compile(Source source) {
    var resolution = Resolution.of(source.name);
    var lexemes    = Lexer.lex(resolution, source);
    var node       = Parser.parse(resolution, lexemes);
    if (node == null) {
      return;
    }
    var solution = Resolver.resolve(resolution, node);
    if (solution == null) {
      return;
    }
    var entity = Analyzer.analyze(resolution, solution, node);
    if (entity == null) {
      return;
    }
    Generator.generate(resolution, Path.of("build"), entity);
    resolution.report();
  }

  /** Process the source file at the given relative path. */
  public static void process(String name) {
    try {
      Launcher.compile(Source.of(name));
    } catch (Exception e) {
      System.out.printf("Could not process %s!%nError: %s%n", name,
        e.getLocalizedMessage());
      e.printStackTrace();
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
