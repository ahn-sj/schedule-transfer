package tally.transfer.transaction.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionStatus {

    PENDING("처리 대기 중"),
    PROCESSING("처리 중"),
    SETTLED("송금 성공"),
    FAILED("송금 실패"),
    CANCELLED("송금 취소됨"),
    ;

    private final String description;
}
