package tally.transfer.transaction.domain.exception;

import tally.transfer.common.exception.ErrorCode;

public class InvalidScheduleDateException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidScheduleDateException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public InvalidScheduleDateException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
