package tally.transfer.account.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class MoneyTest {

    @ParameterizedTest(name = "[실패] 현재 금액이 주어진 금액과 같거나 작으면 false를 반환한다 input = {0}")
    @ValueSource(doubles = {
            10_000, 9_999, 0
    })
    void isGreaterThanFail(final double amount) {
        // given:
        final Money current = Money.wons(amount);
        final Money money = Money.wons(10_000);

        // when:
        final boolean greaterThan = current.isGreaterThan(money);

        // then:
        assertThat(greaterThan).isFalse();
    }

    @ParameterizedTest(name = "[성공] 현재 금액이 주어진 금액과 같거나 크면 true를 반환한다 input = {0}")
    @ValueSource(doubles = {
            10_000, 10_001
    })
    void isGreaterThanOrEqualTo(final double amount) {
        // given:
        final Money current = Money.wons(amount);
        final Money money = Money.wons(10_000);

        // when:
        final boolean greaterThan = current.isGreaterThanOrEqualTo(money);

        // then:
        assertThat(greaterThan).isTrue();
    }

    @Test
    @DisplayName("[성공] 현재 금액과 주어진 금액을 더하면 새로운 Money 객체에 저장된다")
    void plus() {
        // given:
        final Money current = Money.wons(10_000);
        final Money money = Money.wons(5_000);

        // when:
        final Money result = current.plus(money);

        // then:
        assertThat(result).isEqualTo(Money.wons(15_000));
    }

    @Test
    @DisplayName("[성공] 현재 금액에서 주어진 금액을 빼면 새로운 Money 객체에 저장된다")
    void minus() {
        // given:
        final Money current = Money.wons(10_000);
        final Money money = Money.wons(5_000);

        // when:
        final Money result = current.minus(money);

        // then:
        assertThat(result).isEqualTo(Money.wons(5_000));
    }
}