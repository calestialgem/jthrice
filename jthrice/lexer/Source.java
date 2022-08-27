package jthrice.lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/** A UTF-8 source file. */
public class Source {
    /** Path to the source file. */
    public final String path;
    /** Contents of the source file. */
    public final String contents;

    /** Initialize from the file at the given path. */
    public Source(Path path) throws IOException {
        this.path = path.toAbsolutePath().toString();
        contents = Files.readString(path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, path);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Source)) {
            return false;
        }
        Source other = (Source) obj;
        return Objects.equals(contents, other.contents) && Objects.equals(path, other.path);
    }
}
