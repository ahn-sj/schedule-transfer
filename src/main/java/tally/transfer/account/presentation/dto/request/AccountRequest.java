package tally.transfer.account.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tally.transfer.account.domain.enums.AccountType;

public class AccountRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAccountRequest {
        private Long userId;
        private AccountType accountType;
    }
}
