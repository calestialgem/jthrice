// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

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
