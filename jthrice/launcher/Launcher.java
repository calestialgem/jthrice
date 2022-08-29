// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import jthrice.generator.Generator;

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
        try {
            Files.createDirectories(Paths.get("build"));
        } catch (IOException e) {
            System.out.println("Could not create the build directory!");
            e.printStackTrace();
            return;
        }
        for (String argument : arguments) {
            final String FILE_EXTENSION = ".tr";
            if (!argument.endsWith(FILE_EXTENSION)) {
                System.out.println("The Thrice source files should have `" + FILE_EXTENSION + "` file extension!");
                continue;
            }
            Source source = null;
            try {
                source = new Source(Paths.get(argument));
            } catch (IOException e) {
                System.out.println("Could not read the file!");
                e.printStackTrace();
                continue;
            }
            Resolution resolution = new Resolution(source);
            String code = Generator.generate(resolution);
            if (resolution.errors() > 0) {
                System.out.printf("There were %d errors in %s!%n", resolution.errors(), source.path);
            }
            if (resolution.warnings() > 0) {
                System.out.printf("There were %d warnings in %s!%n", resolution.warnings(), source.path);
            }
            try (PrintStream out = new PrintStream(Files.newOutputStream(
                    Paths.get("build/" + argument.substring(0, argument.length() - FILE_EXTENSION.length())
                            + ".c")))) {
                out.println(code);
            } catch (IOException e) {
                System.out.println("Could not write the output file!");
                e.printStackTrace();
                continue;
            }
        }
    }
}
