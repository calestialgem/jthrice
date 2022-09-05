// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.*;
import java.nio.file.*;

/** A UTF-8 source file. */
public class Source {
  /** Character that represents the end of file. */
  public static final char   EOF       = 0;
  /** File extension of Thrice source files. */
  public static final String EXTENSION = "tr";

  /** Source from the file at the given relative path, which does not have the
   * file extension. */
  public static Source of(String name) throws IOException {
    var path     = Path.of(name + '.' + Source.EXTENSION).toAbsolutePath();
    var contents = Files.readString(path).replace("\r", "") + Source.EOF + '\n';
    return new Source(name, path, contents);
  }

  /** Name of the source file without the file extensions. */
  public final String name;
  /** Path to the source file. */
  public final Path   path;
  /** Contents of the source file. */
  public final String contents;

  /** Constructor. */
  private Source(String name, Path path, String contents) {
    this.name     = name;
    this.path     = path;
    this.contents = contents;
  }

  /** Amount of characters. */
  public int size() {
    return this.contents.length();
  }

  /** Whether there is a character at the given index. */
  public boolean exists(int index) {
    return index >= 0 && index < this.size();
  }

  /** Character at the given index. */
  public char at(int index) {
    return this.contents.charAt(index);
  }

  /** Characters from the given first to the last index. */
  public String sub(int first, int last) {
    return this.contents.substring(first, last + 1);
  }
}
