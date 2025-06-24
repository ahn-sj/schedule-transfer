package tally.transfer.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SCHEDULE_TRANSACTION_LIMIT_EXCEEDED(-1001, ErrorType.LIMIT_EXCEEDED, "예약 이체는 하루 최대 10건까지만 등록할 수 있습니다.")
    ;

    private final int code;
    private final ErrorType errorType;
    private final String message;

}
