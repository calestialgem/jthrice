// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.Objects;

/** Result of an algorithm, which can be valid, invalid or unexisting. */
public sealed abstract class Result<Return> permits Result.Valid<Return>, Result.Invalid<Return>, Result.Unexisting<Return> {
    /** Valid result. */
    public static <Return> Result<Return> of(Return value) {
        return new Valid<Return>(value);
    }

    /** Invalid result. */
    public static <Return> Result<Return> ofInvalid() {
        return new Invalid<Return>();
    }

    /** Unexisting result. */
    public static <Return> Result<Return> ofUnexisting() {
        return new Unexisting<Return>();
    }

    /** The first existing result from the given ones. */
    @SafeVarargs
    public static <Return> Result<Return> or(Result<Return>... results) {
        Bug.check(results.length >= 1, "There must be atleast one result!");
        for (var result : results) {
            if (result.exists()) {
                return result;
            }
        }
        return ofUnexisting();
    }

    /** Result of finishing successfully. */
    public static final class Valid<Return> extends Result<Return> {
        /** Return value. */
        public final Return value;

        public Valid(Return value) {
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
        public Return get() {
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
    public static final class Invalid<Return> extends Result<Return> {
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
        public Return get() {
            Bug.unreachable("Invalid!");
            return null;
        }
    }

    /** Result of returning in the begining because the data is not there. */
    public static final class Unexisting<Return> extends Result<Return> {
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
        public Return get() {
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
    public abstract Return get();

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass());
    }
}
