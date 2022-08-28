// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import jthrice.lexer.Lexer;
import jthrice.lexer.Token;

/** Launches the compiler. */
public class Launcher {
    public static void main(String[] arguments) {
        System.out.println("Thrice Java Compiler v.0.0.1");
        System.out.print("Java Version: ");
        System.out.println(System.getProperty("java.version"));
        System.out.println("Running with arguments:");
        for (int i = 0; i < arguments.length; i++) {
            System.out.printf("[%d] %s%n", i, arguments[i]);
        }
        System.out.println();

        if (arguments.length < 1) {
            System.out.println("Provide a Thrice file!");
        }

        for (String argument : arguments) {
            try {
                Source source = new Source(Paths.get(argument));
                ArrayList<Token> tokens = Lexer.lex(source);
                if (source.errors() > 0) {
                    System.out.printf("There were %d errors in %s!%n", source.errors(), source.path);
                }
                if (source.warnings() > 0) {
                    System.out.printf("There were %d warnings in %s!%n", source.warnings(), source.path);
                }
                for (Token token : tokens) {
                    System.out.printf("%s%n", token);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
