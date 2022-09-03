// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.launcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import jthrice.utility.Bug;

/** A UTF-8 source file. */
public class Source {
  /** Character that represents the end of file. */
  public static final char   EOF       = 0;
  /** File extension of Thrice source files. */
  public static final String EXTENSION = "tr";

  /** Name of the source file without the file extensions. */
  public final String name;
  /** Path to the source file. */
  public final Path   path;
  /** Contents of the source file. */
  public final String contents;

  public Source(String name) throws IOException {
    this.name     = name;
    this.path     = Path.of(name + '.' + Source.EXTENSION).toAbsolutePath();
    this.contents = Files.readString(this.path).replace("\r", "") + Source.EOF
      + '\n';
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
    Bug.check(this.exists(index), "Index out of contents!");
    return this.contents.charAt(index);
  }

  /** Characters from the given first to the last index. */
  public String sub(int first, int last) {
    Bug.check(this.exists(first), "First index out of contents!");
    Bug.check(this.exists(last), "Last index out of contents!");
    return this.contents.substring(first, last + 1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.contents, this.path);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Source other)) {
      return false;
    }
    return Objects.equals(this.contents, other.contents)
      && Objects.equals(this.path, other.path);
  }
}
