package tally.transfer.account.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tally.transfer.account.domain.vo.Money;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("[성공] 계좌의 잔액이 주어진 금액보다 크면 출금이 가능하다")
    void canWithdraw() {
        // given:
        final Account account = AccountFixture.anAccount()
                .withBalance(Money.wons(5_000))
                .build();

        // when:
        final boolean canWithdraw = account.canWithdraw(Money.wons(5_000));

        // then:
        assertTrue(canWithdraw);
    }

    @Test
    @DisplayName("[실패] 계좌의 잔액이 주어진 금액보다 작으면 출금이 불가능하다")
    void canWithdraw_fail() {
        // given:
        final Account account = AccountFixture.anAccount()
                .withBalance(Money.wons(5_000))
                .build();

        // when:
        final boolean canWithdraw = account.canWithdraw(Money.wons(5_001));

        // then:
        assertFalse(canWithdraw);
    }

}