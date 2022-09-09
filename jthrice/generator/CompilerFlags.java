// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

import java.io.*;
import java.nio.file.*;

import jthrice.launcher.*;

final class CompilerFlags {
  private final Resolution resolution;
  private final String     command;
  private final Path       build;
  private final Path       code;
  private final Path       executable;

  CompilerFlags(Resolution resolution, String command, Path build) {
    this.resolution = resolution;
    this.command    = command;
    this.build      = build;
    code            = build.resolve(resolution.name + ".c");
    executable      = build.resolve(resolution.name + ".exe");
  }

  void write(String output) {
    try {
      Files.createDirectories(build);
    } catch (IOException e) {
      resolution.error("GENERATOR", "Could not create build directory!");
      e.printStackTrace();
      return;
    }
    try (var out = new PrintStream(Files.newOutputStream(code))) {
      out.print(output);
    } catch (IOException e) {
      resolution.error("GENERATOR", "Could not create output file!");
      e.printStackTrace();
      return;
    }
  }

  void compile() {
    var builder = new ProcessBuilder(command, "-o",
      executable.toAbsolutePath().toString(), code.toAbsolutePath().toString());
    builder.redirectErrorStream(true);
    Process process = null;
    try {
      process = builder.start();
    } catch (IOException e) {
      resolution.error("GENERATOR", "Could not run to compile command!");
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
