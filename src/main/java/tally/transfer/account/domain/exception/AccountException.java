package tally.transfer.account.domain.exception;

import tally.transfer.common.exception.ErrorCode;

public class AccountException extends RuntimeException {

    private final ErrorCode errorCode;

    public AccountException(final ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static class AccountNotFoundException extends AccountException {
        public AccountNotFoundException(final ErrorCode errorCode, final String message) {
            super(errorCode, message);
        }
    }
}
