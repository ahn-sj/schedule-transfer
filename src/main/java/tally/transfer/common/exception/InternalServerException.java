package tally.transfer.common.exception;

public class InternalServerException extends RuntimeException {
    private final ErrorCode errorCode;

    public InternalServerException(final ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
