// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.Objects;
import java.util.function.Supplier;

/** Result of an algorithm, which can be valid, invalid or unexisting. */
public sealed abstract class Result<T> permits Result.Valid<T>, Result.Invalid<T>, Result.Unexisting<T> {
    /** Valid result. */
    public static <T> Result<T> of(T value) {
        return new Valid<T>(value);
    }

    /** Invalid result. */
    public static <T> Result<T> ofInvalid() {
        return new Invalid<T>();
    }

    /** Unexisting result. */
    public static <T> Result<T> ofUnexisting() {
        return new Unexisting<T>();
    }

    /** The first existing result from the given ones. */
    @SafeVarargs
    public static <T> Result<T> or(Supplier<Result<T>>... resultSuppliers) {
        Bug.check(resultSuppliers.length >= 1, "There must be atleast one result supplier!");
        for (var resultSupplier : resultSuppliers) {
            var result = resultSupplier.get();
            if (result.exists()) {
                return result;
            }
        }
        return ofUnexisting();
    }

    /** Result of finishing successfully. */
    public static final class Valid<T> extends Result<T> {
        /** Return value. */
        public final T value;

        public Valid(T value) {
            this.value = value;
        }

        @Override
        public boolean valid() {
            return true;
        }

        @Override
        public boolean invalid() {
            return false;
        }

        @Override
        public boolean unexisting() {
            return false;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + Objects.hash(value);
            return result;
        }

        @Override
        @SuppressWarnings("rawtypes")
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (!(obj instanceof Valid)) {
                return false;
            }
            Valid other = (Valid) obj;
            return Objects.equals(value, other.value);
        }
    }

    /** Result of retuning half way because the input data is wrong. */
    public static final class Invalid<T> extends Result<T> {
        @Override
        public boolean valid() {
            return false;
        }

        @Override
        public boolean invalid() {
            return true;
        }

        @Override
        public boolean unexisting() {
            return false;
        }

        @Override
        public T get() {
            Bug.unreachable("Invalid!");
            return null;
        }
    }

    /** Result of returning in the begining because the data is not there. */
    public static final class Unexisting<T> extends Result<T> {
        @Override
        public boolean valid() {
            return false;
        }

        @Override
        public boolean invalid() {
            return false;
        }

        @Override
        public boolean unexisting() {
            return true;
        }

        @Override
        public T get() {
            Bug.unreachable("Unexisting!");
            return null;
        }
    }

    /** Wheter the return value is valid. */
    public abstract boolean valid();

    /** Whether the return value is invalid. */
    public abstract boolean invalid();

    /** Whether the return value is unexisting. */
    public abstract boolean unexisting();

    /** Whether there is not a valid return value. */
    public boolean empty() {
        return !valid();
    }

    /** Whether there is a resolution valid or not. */
    public boolean exists() {
        return !unexisting();
    }

    /** Return value. */
    public abstract T get();

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass());
    }
}