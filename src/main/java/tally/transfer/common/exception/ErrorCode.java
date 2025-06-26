package tally.transfer.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // USER
    USER_NOT_EXISTS(-1001, ErrorType.NOT_EXISTS, "존재하지 않는 사용자입니다."),

    // ACCOUNT
    ACCOUNT_NOT_FOUND_ERROR(-2001, ErrorType.NOT_EXISTS, "존재하지 않는 계좌입니다."),

    // TRANSACTION,
    SCHEDULE_TRANSACTION_LIMIT_EXCEEDED(-4001, ErrorType.LIMIT_EXCEEDED, "예약 이체는 하루 최대 10건까지만 등록할 수 있습니다."),
    SCHEDULE_AMOUNT_EXCEED_ERROR(-4002, ErrorType.LIMIT_EXCEEDED, "예약 이체 금액을 초과하였습니다."),
    SAME_ACCOUNT_ERROR(-4003, ErrorType.INVALID_REQUEST, "출금 계좌와 입금 계좌가 동일합니다."),
    SCHEDULE_TRANSACTION_CONCURRENT_MODIFICATION_ERROR(-4004, ErrorType.CONCURRENT_MODIFICATION, "예약 이체 처리 중 충돌이 발생했습니다. 잠시 후 다시 시도해주세요."),

    // BANK,
    BANK_MAINTENANCE_ERROR(-9001, ErrorType.MAINTENANCE, "은행 점검 중입니다. 잠시 후 다시 시도해주세요."),

    // COMMON
    REDIS_FAILURE(-10000, ErrorType.REDIS_FAILURE, "Redis 처리 과정에서 오류가 발생했습니다."),
    ;

    private final int code;
    private final ErrorType errorType;
    private final String message;

}
