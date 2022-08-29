// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.generator;

import jthrice.analyzer.Analyzer;
import jthrice.launcher.Resolution;
import jthrice.parser.Syntax;

/** Generates the C source from the syntax tree. */
public class Generator {
    /** Generate the C source from the source in the given resolution. */
    public static String generate(Resolution resolution) {
        Generator generator = new Generator(resolution, Analyzer.analyze(resolution));
        generator.generate();
        return generator.collect();
    }

    /** Resolution of the generated syntax object. */
    private final Resolution resolution;
    /** Top-level syntax object. */
    private final Syntax syntax;
    /** Buffer to append the code into. */
    private final StringBuilder buffer;

    private Generator(Resolution resolution, Syntax syntax) {
        this.resolution = resolution;
        this.syntax = syntax;
        buffer = new StringBuilder();
    }

    /** Generate the code for the top-level syntax object. */
    private void generate() {
        generate(syntax);
    }

    /** Generate the code for the given syntax object. */
    private void generate(Syntax root) {
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
                for (Syntax child : root.childeren()) {
                    generate(child);
                    buffer.append(System.lineSeparator());
                }
                buffer.append("""
                        }
                        """);
                return;

            case DEFINITION:
                buffer.append("int ");
                for (Syntax child : root.childeren()) {
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
                for (Syntax child : root.childeren()) {
                    buffer.append("\t");
                    generate(child);
                }
                return;
            case DECLERATION:
            case EXPRESSION:
            case LITERAL:
            case UNARY_OPERATION:
            case BINARY_OPERATION:
                for (Syntax child : root.childeren()) {
                    generate(child);
                }
                return;
        }
    }

    /** Result of generation. */
    private String collect() {
        return buffer.toString();
    }
}
