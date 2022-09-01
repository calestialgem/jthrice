// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

/** Smallest meaningful group of characters in a source. */
public sealed abstract class Token permits Token.Mark, Token.Number, Token.Keyword, Token.Identifier {
    /** Token at the given location. */
    public static Optional<Token> of(Location first) {
        Optional<Token> mark = Mark.of(first);
        if (mark.isPresent()) {
            return mark;
        }
        Optional<Token> number = Number.of(first);
        if (number.isPresent()) {
            return number;
        }
        Optional<Token> identifier = Identifier.of(first);
        if (identifier.isPresent()) {
            Optional<Token> keyword = Keyword.of((Identifier) identifier.get());
            if (keyword.isPresent()) {
                return keyword;
            }
            return identifier;
        }
        return Optional.empty();
    }

    /** Non-alphanumeric tokens. */
    public static sealed abstract class Mark extends
            Token permits Mark.Plus, Mark.Minus, Mark.Star, Mark.ForwardSlash, Mark.Percent, Mark.Equal, Mark.Colon, Mark.Semicolon, Mark.OpeningBracket, Mark.ClosingBracket, Mark.EOF {
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
                case '(' -> Optional.of(new OpeningBracket(new Portion(first, first)));
                case ')' -> Optional.of(new ClosingBracket(new Portion(first, first)));
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

        /** Opening bracket. */
        public static final class OpeningBracket extends Mark {
            public OpeningBracket(Portion portion) {
                super(portion);
            }
        }

        /** Closing bracket. */
        public static final class ClosingBracket extends Mark {
            public ClosingBracket(Portion portion) {
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

    /** Number literal. */
    public static sealed abstract class Number extends Token permits Number.Integer, Number.Real {
        /** Number at the given location. */
        public static Optional<Token> of(Location first) {
            final String DIGITS = "0123456789";
            final BigInteger BASE = BigInteger.valueOf(DIGITS.length());

            Location current = first;
            int digit = DIGITS.indexOf(current.get());
            if (digit == -1) {
                return Optional.empty();
            }

            Optional<java.lang.Integer> decimalPlaces = Optional.empty();
            BigInteger value = BigInteger.valueOf(0);
            while (digit != -1) {
                value = value.multiply(BASE).add(BigInteger.valueOf(digit));
                if (decimalPlaces.isPresent()) {
                    decimalPlaces = Optional.of(decimalPlaces.get() + 1);
                }
                Optional<Location> next = current.next();
                if (next.isEmpty()) {
                    break;
                }
                current = next.get();
                if (current.get() == '.') {
                    if (decimalPlaces.isPresent()) {
                        return Optional.empty();
                    }
                    decimalPlaces = Optional.of(0);
                    next = current.next();
                    if (next.isEmpty()) {
                        return Optional.empty();
                    }
                    current = next.get();
                }
                digit = DIGITS.indexOf(current.get());
            }

            Portion portion = new Portion(first, current.previous().get());

            if (decimalPlaces.isEmpty()) {
                return Optional.of(new Integer(portion, value));
            }
            if (decimalPlaces.get() == 0) {
                return Optional.empty();
            }
            return Optional.of(new Real(portion, new BigDecimal(value, decimalPlaces.get())));
        }

        /** Integer literal. */
        public static final class Integer extends Number {
            /** Value. */
            public final BigInteger value;

            public Integer(Portion portion, BigInteger value) {
                super(portion);
                this.value = value;
            }

            @Override
            public int hashCode() {
                return Objects.hash(value);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Integer)) {
                    return false;
                }
                Integer other = (Integer) obj;
                return Objects.equals(value, other.value);
            }
        }

        /** Real literal. */
        public static final class Real extends Number {
            /** Value. */
            public final BigDecimal value;

            public Real(Portion portion, BigDecimal value) {
                super(portion);
                this.value = value;
            }

            @Override
            public int hashCode() {
                return Objects.hash(value);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Real)) {
                    return false;
                }
                Real other = (Real) obj;
                return Objects.equals(value, other.value);
            }
        }

        public Number(Portion portion) {
            super(portion);
        }
    }

    /** Reserved identifier. */
    public static sealed abstract class Keyword extends
            Token permits Keyword.I1, Keyword.I2, Keyword.I4, Keyword.I8, Keyword.IX, Keyword.U1, Keyword.U2, Keyword.U4, Keyword.U8, Keyword.UX, Keyword.F4, Keyword.F8 {
        /** Keyword from the identifier. */
        public static Optional<Token> of(Identifier identifier) {
            return switch (identifier.value) {
                case "i1" -> Optional.of(new I1(identifier.portion));
                case "i2" -> Optional.of(new I2(identifier.portion));
                case "i4" -> Optional.of(new I4(identifier.portion));
                case "i8" -> Optional.of(new I8(identifier.portion));
                case "ix" -> Optional.of(new IX(identifier.portion));
                case "u1" -> Optional.of(new U1(identifier.portion));
                case "u2" -> Optional.of(new U2(identifier.portion));
                case "u4" -> Optional.of(new U4(identifier.portion));
                case "u8" -> Optional.of(new U8(identifier.portion));
                case "ux" -> Optional.of(new UX(identifier.portion));
                case "f4" -> Optional.of(new F4(identifier.portion));
                case "f8" -> Optional.of(new F8(identifier.portion));
                default -> Optional.empty();
            };
        }

        /** Keyword `i1`; 1 byte, signed integer type. */
        public static final class I1 extends Keyword {
            public I1(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `i2`; 2 byte, signed integer type. */
        public static final class I2 extends Keyword {
            public I2(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `i4`; 4 byte, signed integer type. */
        public static final class I4 extends Keyword {
            public I4(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `i8`; 8 byte, signed integer type. */
        public static final class I8 extends Keyword {
            public I8(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `ix`; platform-pointer size, signed integer type. */
        public static final class IX extends Keyword {
            public IX(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `u1`; 1 byte, unsigned integer type. */
        public static final class U1 extends Keyword {
            public U1(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `u2`; 2 byte, unsigned integer type. */
        public static final class U2 extends Keyword {
            public U2(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `u4`; 4 byte, unsigned integer type. */
        public static final class U4 extends Keyword {
            public U4(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `u8`; 8 byte, unsigned integer type. */
        public static final class U8 extends Keyword {
            public U8(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `ux`; platform-pointer size, unsigned integer type. */
        public static final class UX extends Keyword {
            public UX(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `f4`; 4 byte, floating-point real. */
        public static final class F4 extends Keyword {
            public F4(Portion portion) {
                super(portion);
            }
        }

        /** Keyword `f8`; 8 byte, floating-point real. */
        public static final class F8 extends Keyword {
            public F8(Portion portion) {
                super(portion);
            }
        }

        public Keyword(Portion portion) {
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

    /** Symbol name in definition or reference. */
    public static final class Identifier extends Token {
        /** Identifier at the given location. */
        public static Optional<Token> of(Location first) {
            Location current = first;
            if ((current.get() < 'a' || current.get() > 'z') && (current.get() < 'A' || current.get() > 'Z')
                    && current.get() != '_') {
                return Optional.empty();
            }

            StringBuilder builder = new StringBuilder();
            while ((current.get() >= '0' && current.get() <= '9') || (current.get() >= 'a' && current.get() <= 'z')
                    || (current.get() >= 'A' && current.get() <= 'Z') || current.get() == '_') {
                builder.append(current.get());
                Optional<Location> next = current.next();
                if (next.isEmpty()) {
                    break;
                }
                current = next.get();
            }

            String value = builder.toString();
            Portion portion = new Portion(first, current.previous().get());
            return Optional.of(new Identifier(portion, value));
        }

        /** Value of the identifier. */
        public final String value;

        public Identifier(Portion portion, String value) {
            super(portion);
            this.value = value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Identifier)) {
                return false;
            }
            Identifier other = (Identifier) obj;
            return Objects.equals(value, other.value);
        }
    }

    /** Portion in the source. */
    public final Portion portion;

    public Token(Portion portion) {
        this.portion = portion;
    }

    @Override
    public String toString() {
        return portion.toString();
    }
}
