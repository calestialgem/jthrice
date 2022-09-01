// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.analyzer;

import java.util.Arrays;
import java.util.Objects;

/** Type of a value in Thrice. */
public sealed abstract class Type permits Type.Scalar, Type.Pointer, Type.Array, Type.Custom, Type.Meta {
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
    public static Array ofArray(Type element, long length) {
        return new Array(element, length);
    }

    /** Product type. */
    public static Custom ofStruct(String name, Type[] members) {
        return new Custom.Struct(name, members);
    }

    /** Sum type. */
    public static Custom ofUnion(String name, Type[] members) {
        return new Custom.Union(name, members);
    }

    /** Finite value type. */
    public static Custom ofEnum(String name, String[] literals) {
        return new Custom.Enum(name, literals);
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
        /** Type of elements. */
        public final Type element;
        /** Amount of elements. */
        public final long length;

        public Array(Type element, long length) {
            this.element = element;
            this.length = length;
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
    public static sealed abstract class Custom extends Type permits Custom.Struct, Custom.Union, Custom.Enum {
        /** Product type. */
        public static final class Struct extends Custom {
            /** Types of members. */
            private final Type[] members;

            public Struct(String name, Type[] members) {
                super(name);
                this.members = members;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + Arrays.hashCode(members);
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Struct)) {
                    return false;
                }
                Struct other = (Struct) obj;
                return Arrays.equals(members, other.members);
            }

        }

        /** Sum type. */
        public static final class Union extends Custom {
            /** Types of members. */
            private final Type[] members;

            public Union(String name, Type[] members) {
                super(name);
                this.members = members;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + Arrays.hashCode(members);
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Union)) {
                    return false;
                }
                Union other = (Union) obj;
                return Arrays.equals(members, other.members);
            }
        }

        /** Finitely valued. */
        public static final class Enum extends Custom {
            /** Names of literals. */
            private final String[] literals;

            public Enum(String name, String[] literals) {
                super(name);
                this.literals = literals;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + Arrays.hashCode(literals);
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Enum)) {
                    return false;
                }
                Enum other = (Enum) obj;
                return Arrays.equals(literals, other.literals);
            }
        }

        /** Name of the type. */
        public final String name;

        public Custom(String name) {
            this.name = name;
        }
    }

    /** Type type. */
    public static final class Meta extends Type {
        /** Value. */
        public final Type value;

        public Meta(Type value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "type";
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
            if (!(obj instanceof Meta)) {
                return false;
            }
            Meta other = (Meta) obj;
            return Objects.equals(value, other.value);
        }
    }
}
