// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.Objects;
import java.util.Optional;

/** Reserved identifier. */
public sealed abstract class Keyword extends
        Token permits Keyword.Let, Keyword.I1, Keyword.I2, Keyword.I4, Keyword.I8, Keyword.IX, Keyword.U1, Keyword.U2, Keyword.U4, Keyword.U8, Keyword.UX, Keyword.F4, Keyword.F8 {
    /** Keyword from the identifier. */
    public static Optional<Token> of(Identifier identifier) {
        return switch (identifier.value) {
            case "let" -> Optional.of(new Let(identifier.portion));
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

    /** Keyword `let`; indicates a local variable definition. */
    public static final class Let extends Keyword {
        public Let(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Let)) {
                return false;
            }
            Let other = (Let) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `i1`; 1 byte, signed integer type. */
    public static final class I1 extends Keyword {
        public I1(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof I1)) {
                return false;
            }
            I1 other = (I1) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `i2`; 2 byte, signed integer type. */
    public static final class I2 extends Keyword {
        public I2(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof I2)) {
                return false;
            }
            I2 other = (I2) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `i4`; 4 byte, signed integer type. */
    public static final class I4 extends Keyword {
        public I4(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof I4)) {
                return false;
            }
            I4 other = (I4) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `i8`; 8 byte, signed integer type. */
    public static final class I8 extends Keyword {
        public I8(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof I8)) {
                return false;
            }
            I8 other = (I8) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `ix`; platform-pointer size, signed integer type. */
    public static final class IX extends Keyword {
        public IX(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof IX)) {
                return false;
            }
            IX other = (IX) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `u1`; 1 byte, unsigned integer type. */
    public static final class U1 extends Keyword {
        public U1(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof U1)) {
                return false;
            }
            U1 other = (U1) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `u2`; 2 byte, unsigned integer type. */
    public static final class U2 extends Keyword {
        public U2(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof U2)) {
                return false;
            }
            U2 other = (U2) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `u4`; 4 byte, unsigned integer type. */
    public static final class U4 extends Keyword {
        public U4(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof U4)) {
                return false;
            }
            U4 other = (U4) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `u8`; 8 byte, unsigned integer type. */
    public static final class U8 extends Keyword {
        public U8(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof U8)) {
                return false;
            }
            U8 other = (U8) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `ux`; platform-pointer size, unsigned integer type. */
    public static final class UX extends Keyword {
        public UX(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof UX)) {
                return false;
            }
            UX other = (UX) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `f4`; 4 byte, floating-point real. */
    public static final class F4 extends Keyword {
        public F4(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof F4)) {
                return false;
            }
            F4 other = (F4) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    /** Keyword `f8`; 8 byte, floating-point real. */
    public static final class F8 extends Keyword {
        public F8(Portion portion) {
            super(portion);
        }

        @Override
        public int hashCode() {
            return Objects.hash(portion);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof F8)) {
                return false;
            }
            F8 other = (F8) obj;
            return Objects.equals(portion, other.portion);
        }
    }

    public Keyword(Portion portion) {
        super(portion);
    }
}
