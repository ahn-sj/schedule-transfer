package tally.transfer.account.domain;

import tally.transfer.account.domain.enums.AccountType;
import tally.transfer.account.domain.vo.Money;

public class AccountFixture {

    private Long id = null;
    private Long userId = null;
    private AccountType type = AccountType.SAVINGS;
    private Money amount = Money.ZERO;

    public static AccountFixture anAccount() {
        return new AccountFixture();
    }

    public AccountFixture withId(Long id) {
        this.id = id;
        return this;
    }

    public AccountFixture withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public AccountFixture withType(AccountType type) {
        this.type = type;
        return this;
    }

    public AccountFixture withBalance(Money amount) {
        this.amount = amount;
        return this;
    }

    public Account build() {
        return new Account(
                id,
                userId,
                type,
                amount
        );
    }
}
