package tally.transfer.transaction.domain.policy.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tally.transfer.account.domain.vo.Money;
import tally.transfer.transaction.domain.policy.ScheduleAmountPolicy;

class BasicGradeAmountPolicyTest {

    @Test
    @DisplayName("[성공] 기본 등급 정책은 2,000,000원 이하의 금액을 허용한다")
    void basicGradeAmountPolicy() {
        // given:
        ScheduleAmountPolicy policy = new BasicGradeAmountPolicy();
        final int LIMIT = 2_000_000;

        // when:
        boolean isAllowed = policy.isAllowed(Money.wons(LIMIT));

        // then:
        Assertions.assertThat(isAllowed).isTrue();
    }

    @Test
    @DisplayName("[실패] 기본 등급 정책은 2,000,001원 이상의 금액을 허용하지 않는다")
    void basicGradeAmountPolicyFail() {
        // given:
        ScheduleAmountPolicy policy = new BasicGradeAmountPolicy();
        final int LIMIT = 2_000_001;

        // when:
        boolean isAllowed = policy.isAllowed(Money.wons(LIMIT));

        // then:
        Assertions.assertThat(isAllowed).isFalse();
    }

}