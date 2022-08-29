// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.parser;

import java.util.ArrayList;

import jthrice.parser.Syntax.Type;

/**
 * Combination of syntax object types that lead to another syntax object type.
 */
public interface Pattern {
    /** Series of types that lead to another one. */
    class Serial implements Pattern {
        /** Types to look for, in order. */
        final Syntax.Type[] input;
        /** Resulting type. */
        final Syntax.Type output;

        public Serial(Type[] input, Type output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public int match(ArrayList<Syntax> stack) {
            if (stack.size() < input.length) {
                return 0;
            }
            for (int i = 0; i < input.length; i++) {
                if (!stack.get(stack.size() - input.length + i).check(input[i])) {
                    return 0;
                }
            }
            return input.length;
        }

        @Override
        public Type result() {
            return output;
        }
    }

    /** Alternative types that lead to another one. */
    class Parallel implements Pattern {
        /** Types to look for. */
        Syntax.Type[] input;
        /** Resulting type. */
        Syntax.Type output;

        public Parallel(Type[] input, Type output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public int match(ArrayList<Syntax> stack) {
            if (stack.size() < 1) {
                return 0;
            }
            if (stack.get(stack.size() - 1).check(input)) {
                return 1;
            }
            return 0;
        }

        @Override
        public Type result() {
            return output;
        }
    }

    /** Repeatition of a type that lead to another one. */
    class Repeatition implements Pattern {
        /** Type to repeat. */
        Syntax.Type repeat;
        /** Type to end the repeatition. */
        Syntax.Type end;
        /** Resulting type. */
        Syntax.Type output;

        public Repeatition(Type repeat, Type end, Type output) {
            this.repeat = repeat;
            this.end = end;
            this.output = output;
        }

        @Override
        public int match(ArrayList<Syntax> stack) {
            if (stack.size() < 2 || !stack.get(stack.size() - 1).check(end)) {
                return 0;
            }
            for (int i = 1; i < stack.size(); i++) {
                if (!stack.get(stack.size() - 1 - i).check(repeat)) {
                    if (i == 1) {
                        return 0;
                    }
                    return i;
                }
            }
            return stack.size();
        }

        @Override
        public Type result() {
            return output;
        }
    }

    /** Pattern that has the given input types in order, one after another. */
    public static Pattern ofSerial(Syntax.Type output, Syntax.Type... input) {
        return new Serial(input, output);
    }

    /** Pattern that has one of the given input types. */
    public static Pattern ofParallel(Syntax.Type output, Syntax.Type... input) {
        return new Parallel(input, output);
    }

    /**
     * Pattern that has the given type repeated as many times as possible before
     * ending with the given type.
     */
    public static Pattern ofRepeatition(Syntax.Type output, Syntax.Type repeat, Syntax.Type end) {
        return new Repeatition(repeat, end, output);
    }

    /**
     * Amount of objects from the top of the stack match the pattern. Zero means no
     * match.
     */
    int match(ArrayList<Syntax> stack);

    /** Resulting type of the pattern. */
    Syntax.Type result();
}
