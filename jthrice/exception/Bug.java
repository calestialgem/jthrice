package jthrice.exception;

/** Exception that shows that there is a bug in the code. */
public class Bug extends RuntimeException {
    /** If the condition does not hold, throw a `Bug` with the given message. */
    public static void check(boolean condition, String message) throws Bug {
        if (!condition) {
            throw new Bug(message);
        }
    }

    /** Initialize with the given message. */
    public Bug(String message) {
        super(message);
    }

    /** Initialize with the given message and the cause. */
    public Bug(String message, Throwable cause) {
        super(message, cause);
    }
}
