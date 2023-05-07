package exception;

/**
 * Thrown to invalid LimitAmount.
 */
public class InvalidLimitAmountException extends RuntimeException {

    public InvalidLimitAmountException() {
        super();
    }

    public InvalidLimitAmountException(String s) {
        super(s);
    }
}
