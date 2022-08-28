// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import jthrice.Logger;
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
                Logger logger = new Logger(System.out, source);
                ArrayList<Token> tokens = Lexer.lex(source, logger);
                for (Token token : tokens) {
                    System.out.print(token.type);
                    System.out.print(" ");
                }
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
