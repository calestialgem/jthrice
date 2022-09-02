// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.utility;

import java.util.Objects;

/** Type of possible early return from a function. */
public sealed abstract class Result<Return> permits Result.Ok<Return>, Result.Error<Return> {
    /** Of correct return. */
    public static <Return> Result<Return> ofOk(Return value) {
        return new Ok<Return>(value);
    }

    /** Of unexisting return value. */
    public static <Return> Result<Return> ofUnexisting() {
        return new Error<Return>(Reason.UNEXISTING);
    }

    /** Of invalid return value. */
    public static <Return> Result<Return> ofInvalid() {
        return new Error<Return>(Reason.INVALID);
    }

    /** Correct return from a function. */
    public static final class Ok<Return> extends Result<Return> {
        /** Returned value. */
        public final Return value;

        public Ok(Return value) {
            this.value = value;
        }

        @Override
        public boolean ok() {
            return true;
        }

        @Override
        public Return get() {
            return value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        @SuppressWarnings("rawtypes")
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Ok)) {
                return false;
            }
            Ok other = (Ok) obj;
            return Objects.equals(value, other.value);
        }
    }

    /** Early return from a function. */
    public static final class Error<Return> extends Result<Return> {
        /** Reason of early return. */
        public final Reason reason;

        public Error(Reason reason) {
            this.reason = reason;
        }

        @Override
        public boolean ok() {
            return false;
        }

        @Override
        public Return get() {
            Bug.unreachable("Asking an unexisting or invalid return value.");
            return null;
        }

        @Override
        public int hashCode() {
            return Objects.hash(reason);
        }

        @Override
        @SuppressWarnings("rawtypes")
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Error)) {
                return false;
            }
            Error other = (Error) obj;
            return reason == other.reason;
        }
    }

    /** Whether the return value is ok. */
    public abstract boolean ok();

    /** Return value. */
    public abstract Return get();
}
