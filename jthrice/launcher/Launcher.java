// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.util.stream.*;

import jthrice.lexer.*;
import jthrice.parser.*;

public final class Launcher {
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

  private static void print(Node node) {
    switch (node) {
      case Root root:
        for (var statement : root.statements) {
          print(statement);
        }
        break;
      case Statement statement:
        switch (statement) {
          case Definition definition:
            System.out.printf("%s: ", definition.name);
            print(definition.type);
            System.out.print(" = ");
            print(definition.value);
            System.out.print(';');
            break;
        }
        System.out.println();
        break;
      case Expression expression:
        switch (expression) {
          case NullaryExpression nullary:
            System.out.print(nullary.operator);
            break;
          case PrenaryExpression prenary:
            System.out.print('[');
            System.out.print(prenary.operator);
            print(prenary.operand);
            System.out.print(']');
            break;
          case PostaryExpression postary:
            System.out.print('[');
            print(postary.operand);
            System.out.print(postary.operator);
            System.out.print(']');
            break;
          case CirnaryExpression cirnary:
            System.out.print(cirnary.left);
            print(cirnary.operand);
            System.out.print(cirnary.right);
            break;
          case BinaryExpression binary:
            System.out.print('[');
            print(binary.left);
            System.out.print(binary.operator);
            print(binary.right);
            System.out.print(']');
            break;
          case VariaryExpression polinary:
            System.out.print('[');
            print(polinary.first);
            System.out.print(polinary.left);
            for (var i = 0; i < polinary.remaining.size() - 1; i++) {
              print(polinary.remaining.get(i));
              System.out.printf("%s ", polinary.between.get(i));
            }
            if (!polinary.remaining.isEmpty()) {
              print(polinary.remaining.get(polinary.remaining.size() - 1));
            }
            System.out.print(polinary.right);
            System.out.print(']');
            break;
        }
        break;
    }
  }

  public static void compile(Source source) {
    var resolution = Resolution.of(source.name());
    var lex        = Lexer.lex(resolution, source);
    var root       = Parser.parse(resolution, lex);
    print(root);
    resolution.report();
  }

  public static void process(String name) {
    try {
      Launcher.compile(Source.of(name));
    } catch (Exception e) {
      System.out.printf("Could not process %s!%nError: %s%n", name,
        e.getLocalizedMessage());
      e.printStackTrace();
    }
  }

  public static void main(String[] arguments) {
    Launcher.printArguments(arguments);
    if (arguments.length < 1) {
      System.out.println("Provide a Thrice file!");
    }
    Stream.of(arguments).parallel().forEach(Launcher::process);
  }

  private Launcher() {
  }
}
