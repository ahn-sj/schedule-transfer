package tally.transfer.transaction.domain.repository;

import tally.transfer.transaction.domain.ScheduleTransaction;

import java.time.LocalDate;

public interface ScheduleTransactionRepository {
    ScheduleTransaction save(ScheduleTransaction scheduleTransaction);
    long countBySourceAndScheduleDt(Long sourceAccountId, LocalDate scheduleDt);
}
