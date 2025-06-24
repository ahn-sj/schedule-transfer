package tally.transfer.transaction.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    DEBIT("출금"),
    CREDIT("입금"),
    ;

    private final String description;
}
