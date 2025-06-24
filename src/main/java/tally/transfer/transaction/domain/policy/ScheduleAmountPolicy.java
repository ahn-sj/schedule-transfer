package tally.transfer.transaction.domain.policy;

import tally.transfer.account.domain.vo.Money;
import tally.transfer.user.domain.enums.UserGrade;

public interface ScheduleAmountPolicy {
    boolean isAllowed(Money amount);
    UserGrade getGrade();
}
