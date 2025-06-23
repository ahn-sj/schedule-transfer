package tally.transfer.account.domain.vo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    @DisplayName("[성공] 현재 금액이 주어진 금액보다 크면 true를 반환한다")
    void isGreaterThan() {
        // given:
        final Money current = Money.wons(10_000);
        final Money money = Money.wons(5_000);

        // when:
        final boolean greaterThan = current.isGreaterThan(money);

        // then:
        Assertions.assertThat(greaterThan).isTrue();
    }

}