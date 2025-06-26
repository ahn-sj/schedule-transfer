package tally.transfer.transaction.domain.exception;

import tally.transfer.common.exception.ErrorCode;

public class TransactionException extends RuntimeException {

    private final ErrorCode errorCode;

    public TransactionException(final ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static class ScheduleAmountExceedException extends TransactionException {
        public ScheduleAmountExceedException(final ErrorCode errorCode, final String message) {
            super(errorCode, message);
        }
    }

    public static class SameAccountException extends TransactionException {
        public SameAccountException(final ErrorCode errorCode, final String message) {
            super(errorCode, message);
        }
    }

    public static class ConcurrentModificationException extends TransactionException {
        public ConcurrentModificationException(final ErrorCode errorCode, final String message) {
            super(errorCode, message);
        }
    }

    public static class ScheduleLimitExceededException extends TransactionException {
        public ScheduleLimitExceededException(final ErrorCode errorCode, final String message) {
            super(errorCode, message);
        }
    }
}
