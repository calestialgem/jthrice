// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.launcher.Resolution;
import jthrice.parser.Node;
import jthrice.parser.Parser;
import jthrice.utility.Result;

/** Analyzes a syntax tree for correctness and optimization. */
public class Analyzer {
    /** Analyze the source in the given resolution. */
    public static Result<Entity.Program> analyze(Resolution resolution) {
        var node = Parser.parse(resolution);
        if (node.empty()) {
            return Result.ofUnexisting();
        }
        Analyzer analyzer = new Analyzer(resolution, node.get());
        return analyzer.analyze();
    }

    /** Resolution of the analyzed syntax object. */
    private final Resolution resolution;
    /** Root node. */
    private final Node.Program nodeRoot;

    public Analyzer(Resolution resolution, Node.Program nodeRoot) {
        this.resolution = resolution;
        this.nodeRoot = nodeRoot;
    }

    private Result<Entity.Program> analyze() {
        return Result.ofUnexisting();
    }
}
