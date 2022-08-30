// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

import java.nio.file.Path;

import jthrice.analyzer.Analyzer;
import jthrice.launcher.Resolution;
import jthrice.parser.Symbol;

/** Generates the C source from the syntax tree. */
public class Generator {
    /** C compiler. */
    public static final String COMPILER = "clang";

    /** Generate the C source from the source in the given resolution. */
    public static void generate(Resolution resolution, Path build) {
        Generator generator = new Generator(resolution, build, Analyzer.analyze(resolution));
        generator.generate();
        generator.collect();
    }

    /** Resolution of the generated syntax object. */
    private final Resolution resolution;
    /** Path to the build folder. */
    private final Path build;
    /** Top-level syntax object. */
    private final Symbol syntax;
    /** Buffer to append the code into. */
    private final StringBuilder buffer;

    private Generator(Resolution resolution, Path build, Symbol syntax) {
        this.resolution = resolution;
        this.build = build;
        this.syntax = syntax;
        buffer = new StringBuilder();
    }

    /** Generate the code for the top-level syntax object. */
    private void generate() {
        generate(syntax);
    }

    /** Generate the code for the given syntax object. */
    private void generate(Symbol root) {
        switch (root.type) {
            case PLUS:
            case MINUS:
            case STAR:
            case FORWARD_SLASH:
            case PERCENT:
            case EQUAL:
            case SEMICOLON:
            case NUMBER:
            case IDENTIFIER:
                buffer.append(root.token(0).value);
                return;
            case LET:
            case EOF:
                return;
            case SOURCE:
                buffer.append("""
                        #include <stdio.h>

                        int main(int argc, char** argv) {
                        """);
                for (Symbol child : root.childeren()) {
                    generate(child);
                    buffer.append(System.lineSeparator());
                }
                buffer.append("""
                        }
                        """);
                return;

            case DEFINITION:
                buffer.append("int ");
                for (Symbol child : root.childeren()) {
                    generate(child);
                }
                buffer.append(System.lineSeparator());
                buffer.append("\tprintf(\"");
                buffer.append((String) root.child(0).child(1).token(0).value);
                buffer.append(" = %d\\n\", ");
                buffer.append((String) root.child(0).child(1).token(0).value);
                buffer.append(");");
                buffer.append(System.lineSeparator());
                return;
            case STATEMENT:
                for (Symbol child : root.childeren()) {
                    buffer.append("\t");
                    generate(child);
                }
                return;
            case DECLERATION:
            case EXPRESSION:
            case LITERAL:
            case UNARY_OPERATION:
            case BINARY_OPERATION:
                for (Symbol child : root.childeren()) {
                    generate(child);
                }
                return;
        }
    }

    /** Result of generation. */
    private void collect() {
        CompilerFlags compilerFlags = new CompilerFlags(resolution, COMPILER, build);
        compilerFlags.write(buffer.toString());
        compilerFlags.compile();
    }
}
