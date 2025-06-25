package tally.transfer.transaction.domain.exception;

import tally.transfer.common.exception.ErrorCode;

public class BankMaintenanceException extends RuntimeException {

    private final ErrorCode errorCode;

    public BankMaintenanceException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
