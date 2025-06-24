package tally.transfer.transaction.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionFailureReason {
    INSUFFICIENT_FUNDS("송신자 잔액부족"),
    DESTINATION_ACCOUNT_NOT_FOUND("수신자 계좌 없음"),
    DESTINATION_ACCOUNT_INACTIVE("수신자 계좌 휴먼 또는 해지됨"),
    LIMIT_EXCEEDED("송금 한도 초과"),
    USER_CANCELLED("사용자에 의해 취소됨"),
    ;

    private final String description;
}
