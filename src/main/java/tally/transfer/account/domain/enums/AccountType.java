package tally.transfer.account.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    SAVINGS("보통예금"),
    INSTALLMENT_SAVINGS("적금"),
    ;

    private final String description;
}
