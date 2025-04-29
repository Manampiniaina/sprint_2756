package toolkit.exception;

public class SQLBuilderException extends Exception{
    public SQLBuilderException(String message) {
        super(message);
    }
    public SQLBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
