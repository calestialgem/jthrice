// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.Objects;

/** Type of a value in Thrice. */
public sealed abstract class Type permits Type.Scalar, Type.Pointer, Type.Array, Type.Aggregate {
    /** Integer type. */
    public static Scalar ofInteger(long size, boolean signedness) {
        if (signedness) {
            return new Scalar.Signed(size);
        } else {
            return new Scalar.Unsigned(size);
        }
    }

    /** Floating-point real type. */
    public static Scalar ofFloating(long size) {
        return new Scalar.Floating(size);
    }

    /** Pointer type. */
    public static Pointer ofPointer(Type pointee) {
        return new Pointer(pointee);
    }

    /** Array type. */
    public static Array ofArray(long length, Type element) {
        return new Array(length, element);
    }

    /** User defined type. */
    public static Aggregate ofAggregate(long size, String name) {
        return new Aggregate(size, name);
    }

    /** Built-in types. */
    public static sealed abstract class Scalar
            extends Type permits Scalar.Signed, Scalar.Unsigned, Scalar.Floating {
        /** Integers including negative ones. */
        public static final class Signed extends Scalar {
            public Signed(long size) {
                super(size);
            }

            @Override
            public String toString() {
                return "i" + size;
            }

            @Override
            public int hashCode() {
                return Objects.hash(size);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Signed)) {
                    return false;
                }
                Signed other = (Signed) obj;
                return size == other.size;
            }
        }

        /** Integers starting from zero. */
        public static final class Unsigned extends Scalar {
            public Unsigned(long size) {
                super(size);
            }

            @Override
            public String toString() {
                return "u" + size;
            }

            @Override
            public int hashCode() {
                return Objects.hash(size);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Unsigned)) {
                    return false;
                }
                Unsigned other = (Unsigned) obj;
                return size == other.size;
            }
        }

        /** Floating point real numbers. */
        public static final class Floating extends Scalar {
            public Floating(long size) {
                super(size);
            }

            @Override
            public String toString() {
                return "f" + size;
            }

            @Override
            public int hashCode() {
                return Objects.hash(size);
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Floating)) {
                    return false;
                }
                Floating other = (Floating) obj;
                return size == other.size;
            }
        }

        /** Amount of bytes in memory. */
        public final long size;

        public Scalar(long size) {
            this.size = size;
        }
    }

    /** Pointer types. */
    public static final class Pointer extends Type {
        /** Type of pointee. */
        public final Type pointee;

        public Pointer(Type pointee) {
            this.pointee = pointee;
        }

        @Override
        public String toString() {
            return pointee + "&";
        }

        @Override
        public int hashCode() {
            return Objects.hash(pointee);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Pointer)) {
                return false;
            }
            Pointer other = (Pointer) obj;
            return Objects.equals(pointee, other.pointee);
        }
    }

    /** Array types. */
    public static final class Array extends Type {
        /** Amount of elements. */
        public final long length;
        /** Type of elements. */
        public final Type element;

        public Array(long length, Type element) {
            this.length = length;
            this.element = element;
        }

        @Override
        public String toString() {
            return element + "[" + length + "]";
        }

        @Override
        public int hashCode() {
            return Objects.hash(element, length);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Array)) {
                return false;
            }
            Array other = (Array) obj;
            return Objects.equals(element, other.element) && length == other.length;
        }
    }

    /** User defined types. */
    public static final class Aggregate extends Type {
        /** Amount of bytes in memory. */
        public final long size;
        /** Name of the type. */
        public final String name;

        public Aggregate(long size, String name) {
            this.size = size;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, size);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Aggregate)) {
                return false;
            }
            Aggregate other = (Aggregate) obj;
            return Objects.equals(name, other.name) && size == other.size;
        }
    }
}
