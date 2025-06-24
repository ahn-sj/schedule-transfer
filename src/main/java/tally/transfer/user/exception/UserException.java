package tally.transfer.user.exception;

import tally.transfer.common.exception.ErrorCode;

public class UserException extends RuntimeException {

    private final ErrorCode errorCode;

    public UserException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public UserException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static class UserNotFoundException extends UserException {
        public UserNotFoundException(final ErrorCode errorCode, final String message) {
            super(errorCode, message);
        }
    }
}
