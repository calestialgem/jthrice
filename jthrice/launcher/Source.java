// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.*;
import java.nio.file.*;

public final class Source {
  public static final char   EOF       = 0;
  public static final String EXTENSION = "tr";

  public static Source of(String name) throws IOException {
    var path     = Path.of(name + '.' + Source.EXTENSION).toAbsolutePath();
    var contents = Files.readString(path).replace("\r", "") + Source.EOF + '\n';
    return new Source(name, path, contents);
  }

  public final String name;
  public final Path   path;
  public final String contents;

  private Source(String name, Path path, String contents) {
    this.name     = name;
    this.path     = path;
    this.contents = contents;
  }

  public int size() {
    return contents.length();
  }

  public boolean exists(int index) {
    return index >= 0 && index < size();
  }

  public char at(int index) {
    return contents.charAt(index);
  }

  public String sub(int first, int last) {
    return contents.substring(first, last + 1);
  }
}
