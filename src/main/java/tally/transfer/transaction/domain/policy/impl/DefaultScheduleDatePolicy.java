package tally.transfer.transaction.domain.policy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tally.transfer.common.exception.ErrorCode;
import tally.transfer.common.provider.DateProvider;
import tally.transfer.transaction.domain.exception.InvalidScheduleDateException;
import tally.transfer.transaction.domain.policy.ScheduleDatePolicy;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DefaultScheduleDatePolicy implements ScheduleDatePolicy {

    private final DateProvider dateProvider;

    @Override
    public void validate(final LocalDate scheduleDt) {
        final LocalDate today = dateProvider.today();

        if(scheduleDt.isBefore(today)) {
            throw new InvalidScheduleDateException(ErrorCode.SCHEDULE_TRANSACTION_LIMIT_EXCEEDED, "예약 날짜는 오늘 또는 이후여야 합니다. input = " + scheduleDt);
        }
    }
}
