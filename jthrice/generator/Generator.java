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

    /** Result of generation. */
    private String collect() {
        return buffer.toString();
    }
}
