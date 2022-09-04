// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

import java.io.*;
import java.nio.file.*;

import jthrice.launcher.*;

/** Command line arguments, which will be passed to the C compiler. */
class CompilerFlags {
  /** Resolution that will be compiled. */
  private final Resolution resolution;
  /** Compiler command. */
  private final String     command;
  /** Path to the build directory. */
  private final Path       build;
  /** Path to the code output file. */
  private final Path       code;
  /** Path to the executable. */
  private final Path       executable;

  CompilerFlags(Resolution resolution, String command, Path build) {
    this.resolution = resolution;
    this.command    = command;
    this.build      = build;
    this.code       = build.resolve(resolution.source.name + ".c");
    this.executable = build.resolve(resolution.source.name + ".exe");
  }

  /** Write the given C source code to the output file. */
  void write(String output) {
    try {
      Files.createDirectories(this.build);
    } catch (IOException e) {
      this.resolution.error("GENERATOR", "Could not create build directory!");
      e.printStackTrace();
      return;
    }
    try (var out = new PrintStream(Files.newOutputStream(this.code))) {
      out.print(output);
    } catch (IOException e) {
      this.resolution.error("GENERATOR", "Could not create output file!");
      e.printStackTrace();
      return;
    }
  }

  /** Compile the C source code. */
  void compile() {
    var builder = new ProcessBuilder(this.command, "-o",
      this.executable.toAbsolutePath().toString(),
      this.code.toAbsolutePath().toString());
    builder.redirectErrorStream(true);
    Process process = null;
    try {
      process = builder.start();
    } catch (IOException e) {
      this.resolution.error("GENERATOR", "Could not run to compile command!");
      e.printStackTrace();
      return;
    }
    var reader = new BufferedReader(
      new InputStreamReader(process.getInputStream()));
    while (true) {
      String line = null;
      try {
        line = reader.readLine();
      } catch (IOException e) {
        this.resolution.error("GENERATOR",
          "Could not read the compiler output!");
        e.printStackTrace();
        return;
      }
      if (line == null) {
        break;
      }
      this.resolution.info("COMPILER", line);
    }
  }
}
