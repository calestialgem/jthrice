// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.ArrayList;

import jthrice.Bug;
import jthrice.launcher.Resolution;
import jthrice.parser.Parser;
import jthrice.parser.Syntax;

/** Analyzes a syntax tree for correctness and optimization. */
public class Analyzer {
    /** Analyze the source in the given resolution. */
    public static Syntax analyze(Resolution resolution) {
        Analyzer analyzer = new Analyzer(resolution, Parser.parse(resolution));
        analyzer.findDefinitions();
        analyzer.checkReferences();
        return analyzer.collect();
    }

    /** Resolution of the analyzed syntax object. */
    private final Resolution resolution;
    /** Top-level syntax object. */
    private final Syntax syntax;
    /** The symbols that are defined. */
    private final ArrayList<String> symbols;

    private Analyzer(Resolution resolution, Syntax syntax) {
        this.resolution = resolution;
        this.syntax = syntax;
        symbols = new ArrayList<>();
    }

    /** Find all the defined symbols. */
    private void findDefinitions() {
        for (Syntax statement : syntax.childeren()) {
            if (statement.checkChilderen(Syntax.Type.DEFINITION)) {
                Object identifier = statement.child(0).child(0).child(1).token(0).value;
                Bug.check(identifier instanceof String, "The value under identifier is not a string!");
                symbols.add((String) identifier);
            }
        }
    }

    /** Check all the refered symbols. */
    private void checkReferences() {
        checkReferences(syntax);
    }

    /** Check all the refered symbols under the given syntax object. */
    private void checkReferences(Syntax root) {
        if (root.check(Syntax.Type.EXPRESSION) && root.checkChilderen(Syntax.Type.IDENTIFIER)) {
            Object identifier = root.child(0).token(0).value;
            Bug.check(identifier instanceof String, "The value under identifier is not a string!");
            if (!symbols.contains(identifier)) {
                resolution.error("ANALYZER", root.child(0).token(0).portion, "The refered symbol does not exist!");
            }
        } else {
            for (Syntax child : root.childeren()) {
                checkReferences(child);
            }
        }
    }

    /** Result of analysis. */
    private Syntax collect() {
        return syntax;
    }
}
