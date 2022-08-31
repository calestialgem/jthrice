// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import jthrice.generator.Generator;
import jthrice.parser.Parser;
import jthrice.parser.Syntatic;

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
        Path build = Path.of("build");
        for (String argument : arguments) {
            Resolution resolution = null;
            try {
                resolution = new Resolution(new Source(argument));
            } catch (IOException e) {
                System.out.println("Could not read the file!");
                e.printStackTrace();
                continue;
            }
            // Generator.generate(resolution, build);
            Optional<Syntatic.Source> parse = Parser.parse(resolution);
            if (parse.isPresent()) {
                System.out.println(parse.get());
            }
            if (resolution.errors() > 0) {
                System.out.printf("There were %d errors in %s!%n", resolution.errors(), argument);
            }
            if (resolution.warnings() > 0) {
                System.out.printf("There were %d warnings in %s!%n", resolution.warnings(), argument);
            }
        }
    }
}
