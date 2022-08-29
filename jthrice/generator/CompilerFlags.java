// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import jthrice.launcher.Resolution;

/** Command line arguments, which will be passed to the C compiler. */
class CompilerFlags {
    /** Resolution that will be compiled. */
    private final Resolution resolution;
    /** Compiler command. */
    private final String command;
    /** Path to the build directory. */
    private final Path build;
    /** Path to the code output file. */
    private final Path code;
    /** Path to the executable. */
    private final Path executable;

    CompilerFlags(Resolution resolution, String command, Path build) {
        this.resolution = resolution;
        this.command = command;
        this.build = build;
        code = build.resolve(resolution.source.name + ".c");
        executable = build.resolve(resolution.source.name + ".exe");
    }

    /** Write the given C source code to the output file. */
    void write(String output) {
        try {
            Files.createDirectories(build);
        } catch (IOException e) {
            resolution.error("GENERATOR", "Could not create build directory!");
            e.printStackTrace();
            return;
        }
        try (PrintStream out = new PrintStream(Files.newOutputStream(code))) {
            out.print(output);
        } catch (IOException e) {
            resolution.error("GENERATOR", "Could not create output file!");
            e.printStackTrace();
            return;
        }
    }

    /** Compile the C source code. */
    void compile() {
        ProcessBuilder builder = new ProcessBuilder(command, "-o", executable.toAbsolutePath().toString(),
                code.toAbsolutePath().toString());
        builder.redirectErrorStream(true);
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            resolution.error("GENERATOR", "Could not run to compile command!");
            e.printStackTrace();
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while (true) {
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                resolution.error("GENERATOR", "Could not read the compiler output!");
                e.printStackTrace();
                return;
            }
            if (line == null) {
                break;
            }
            resolution.info("COMPILER", line);
        }
    }
}
