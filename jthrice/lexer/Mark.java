// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Optional;

/** Non-alphanumeric tokens. */
public sealed abstract class Mark extends
        Token permits Mark.Plus, Mark.Minus, Mark.Star, Mark.ForwardSlash, Mark.Percent, Mark.Equal, Mark.Colon, Mark.Semicolon, Mark.EOF {
    /** Mark at the given location. */
    public static Optional<Token> of(Location first) {
        return switch (first.get()) {
            case '+' -> Optional.of(new Plus(new Portion(first, first)));
            case '-' -> Optional.of(new Minus(new Portion(first, first)));
            case '*' -> Optional.of(new Star(new Portion(first, first)));
            case '/' -> Optional.of(new ForwardSlash(new Portion(first, first)));
            case '%' -> Optional.of(new Percent(new Portion(first, first)));
            case '=' -> Optional.of(new Equal(new Portion(first, first)));
            case ':' -> Optional.of(new Colon(new Portion(first, first)));
            case ';' -> Optional.of(new Semicolon(new Portion(first, first)));
            case 0 -> Optional.of(new EOF(new Portion(first, first)));
            default -> Optional.empty();
        };
    }

    /** Plus sign. */
    public static final class Plus extends Mark {
        public Plus(Portion portion) {
            super(portion);
        }
    }

    /** Minus sign. */
    public static final class Minus extends Mark {
        public Minus(Portion portion) {
            super(portion);
        }
    }

    /** Star. */
    public static final class Star extends Mark {
        public Star(Portion portion) {
            super(portion);
        }
    }

    /** Forward slash. */
    public static final class ForwardSlash extends Mark {
        public ForwardSlash(Portion portion) {
            super(portion);
        }
    }

    /** Percent symbol. */
    public static final class Percent extends Mark {
        public Percent(Portion portion) {
            super(portion);
        }
    }

    /** Equal sign. */
    public static final class Equal extends Mark {
        public Equal(Portion portion) {
            super(portion);
        }
    }

    /** Colon. */
    public static final class Colon extends Mark {
        public Colon(Portion portion) {
            super(portion);
        }
    }

    /** Semicolon. */
    public static final class Semicolon extends Mark {
        public Semicolon(Portion portion) {
            super(portion);
        }
    }

    /** End of file character. */
    public static final class EOF extends Mark {
        public EOF(Portion portion) {
            super(portion);
        }
    }

    public Mark(Portion portion) {
        super(portion);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass());
    }
}
