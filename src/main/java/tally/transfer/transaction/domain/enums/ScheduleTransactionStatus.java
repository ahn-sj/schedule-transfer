package tally.transfer.transaction.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleTransactionStatus {
    RESERVED("예약됨"),
    SETTLED("정상처리됨"),
    REJECTED("거절됨"),
    USER_CANCELLED("취소됨"),
    ;

    private final String description;
}
