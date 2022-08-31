// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import jthrice.lexer.Token;

/** Symbol that is the root of all the symbols. */
public final class Start extends Symbol {
    /** From the top of the given stack. */
    public static Optional<Result> of(Stack stack) {
        Optional<Terminal> eof = stack.topTerminal(0, Token.Mark.EOF.class);
        if (eof.isEmpty()) {
            return Optional.empty();
        }
        ArrayList<Statement> statements = new ArrayList<>();
        for (int i = 1; true; i++) {
            Optional<Statement> statement = stack.top(i, Statement.class);
            if (statement.isEmpty()) {
                break;
            }
            statements.add(statement.get());
        }
        return Optional
                .of(new Result(new Start(statements.toArray(new Statement[0]), eof.get()), statements.size() + 1));
    }

    /** Statements under the source file. */
    private final Statement[] statements;
    /** End of the file. */
    public final Terminal eof;

    public Start(Statement[] statements, Terminal eof) {
        this.statements = statements;
        this.eof = eof;
    }

    /** Statements in the source file. */
    public Statement[] statements() {
        return Arrays.copyOf(statements, statements.length);
    }
}
