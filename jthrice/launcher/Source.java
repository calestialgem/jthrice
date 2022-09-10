// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public final class Source {
  public static final char   EOF       = 0;
  public static final String EXTENSION = "tr";

  public static Source of(String name) throws IOException {
    var path     = Path.of(name + '.' + EXTENSION).toAbsolutePath();
    var contents = Files.readString(path).replace("\r", "") + EOF + '\n';
    return new Source(name, contents);
  }

  private final String name;
  private final String contents;

  private Source(String name, String contents) {
    this.name     = name;
    this.contents = contents;
  }

  public int length() {
    return contents.length();
  }

  public boolean exists(int index) {
    return index >= 0 && index < length();
  }

  public char at(int index) {
    return contents.charAt(index);
  }

  public String sub(int first, int last) {
    return contents.substring(first, last + 1);
  }

  public String name() {
    return name;
  }

  public Matcher matcher(Pattern pattern) {
    return pattern.matcher(contents);
  }

  public boolean matches(String string, int index) {
    return contents.startsWith(string, index);
  }
}
