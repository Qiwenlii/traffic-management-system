package tms.util;

public class IntersectionNotFoundException extends Throwable {
    /**
     * Constructs a normal IntersectionNotFoundException with no error message
     * or cause.
     */
    public IntersectionNotFoundException() {
        super();
    }

    /**
     * Constructs an IntersectionNotFoundException that contains a helpful
     * message detailing why the exception occurred.
     * Note: implementing this constructor is optional. It has only been
     * included to ensure your code will compile if you give your exception a
     * message when throwing it. This practice can be useful for debugging
     * purposes.
     *
     * Important: do not write JUnit tests that expect a valid implementation of
     * the assignment to have a certain error message, as the official solution
     * will use different messages to those you are expecting, if any at all.
     *
     * @param message detail message
     */
    public IntersectionNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs an IntersectionNotFoundException that contains a helpful
     * message detailing why the exception occurred and a cause of the exception.
     * Note: implementing this constructor is optional. It has only been
     * included to ensure your code will compile if you give your exception a
     * message when throwing it. This practice can be useful for debugging
     * purposes.
     *
     * Important: do not write JUnit tests that expect a valid implementation of
     * the assignment to have a certain error message, as the official solution
     * will use different messages to those you are expecting, if any at all.
     * @param message detail message
     * @param err cause of the exception
     */
    public IntersectionNotFoundException(String message, Throwable err) {
        super(message, err);
    }
}
