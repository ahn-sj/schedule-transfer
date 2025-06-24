package tally.transfer.transaction.domain;

import org.junit.jupiter.api.Test;
import tally.transfer.account.domain.vo.Money;

import java.time.LocalDate;

class ScheduleTransactionTest {

    @Test
    void schedule() {
        // given:
        final ScheduleTransaction scheduleTransaction = ScheduleTransaction.schedule(
                1L,
                2L,
                Money.wons(1_000),
                LocalDate.of(2024, 1, 1)
        );

        // when:

        // then:
    }

}