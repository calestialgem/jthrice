// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import jthrice.launcher.Resolution;
import jthrice.parser.Node;
import jthrice.parser.Parser;
import jthrice.utility.List;
import jthrice.utility.Result;

/** Creates a program entity from a program node. */
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

    /** Resolution of the analyzed program node. */
    private final Resolution resolution;
    /** Analyzed program node. */
    private final Node.Program node;

    public Analyzer(Resolution resolution, Node.Program node) {
        this.resolution = resolution;
        this.node = node;
    }

    private Result<Entity.Program> analyze() {
        return Result.of(new Entity.Program(List.of(node.statements.stream().map(this::analyzeStatement)
                .filter(Result::valid).map(Result::get).toList())));
    }

    private Result<Entity.Statement> analyzeStatement(Node.Statement statement) {
        return Result.ofUnexisting();
    }
}
