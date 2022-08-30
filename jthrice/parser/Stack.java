// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;
import java.util.Optional;

import jthrice.Bug;
import jthrice.lexer.Token;

/** Stack of symbols. */
public class Stack {
    /** Symbols. */
    private final ArrayList<Symbol> symbols;

    public Stack() {
        symbols = new ArrayList<>();
    }

    /** Push the given terminal to the top of the stack. */
    public void shift(Token token) {
        symbols.add(new Terminal(token));
    }

    /** Pop the elements in the result and push the resulting symbol. */
    public void reduce(Symbol.Result result) {
        Bug.check(symbols.size() >= result.consumed, "Consumed more than the symbol count!");
        for (int i = symbols.size() - 1; i >= 0; i--) {
            symbols.remove(i);
        }
        symbols.add(result.symbol);
    }

    /** Symbol at the given index from the top of the stack. */
    public Optional<Symbol> top(int index) {
        Bug.check(index >= 0, "The index is negative!");
        if (symbols.size() <= index) {
            return Optional.empty();
        }
        return Optional.of(symbols.get(symbols.size() - 1 - index));
    }

    /** Symbol at the given index from the top of the stack with the given type. */
    @SuppressWarnings("unchecked")
    public <T extends Symbol> Optional<T> top(int index, Class<T> type) {
        Optional<Symbol> symbol = top(index);
        if (symbol.isEmpty()) {
            return Optional.empty();
        }
        if (!type.isInstance(symbol.get())) {
            return Optional.empty();
        }
        return Optional.of((T) symbol.get());
    }

    /**
     * Terminal symbol at the given index from the top of the stack with one of the
     * given token types.
     */
    @SafeVarargs
    public final Optional<Terminal> topTerminal(int index, Class<? extends Token>... types) {
        Optional<Terminal> terminal = top(index, Terminal.class);
        if (terminal.isEmpty()) {
            return Optional.empty();
        }
        for (Class<? extends Token> type : types) {
            if (type.isInstance(terminal.get().token)) {
                return terminal;
            }
        }
        return Optional.empty();
    }

    /** Amount of symbols. */
    public int size() {
        return symbols.size();
    }
}
