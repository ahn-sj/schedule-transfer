package tally.transfer.transaction.domain.policy.impl;

import org.springframework.stereotype.Component;
import tally.transfer.account.domain.vo.Money;
import tally.transfer.transaction.domain.policy.ScheduleAmountPolicy;
import tally.transfer.user.domain.enums.UserGrade;

@Component
public class BasicGradeAmountPolicy implements ScheduleAmountPolicy {

    private final Money LIMIT = Money.wons(2_000_000);

    @Override
    public boolean isAllowed(final Money amount) {
        return amount.isLessThanOrEqualTo(LIMIT);
    }

    @Override
    public UserGrade getGrade() {
        return UserGrade.BASIC;
    }
}
