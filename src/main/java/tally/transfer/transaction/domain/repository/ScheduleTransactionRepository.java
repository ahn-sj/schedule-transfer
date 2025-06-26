package tally.transfer.transaction.domain.repository;

import tally.transfer.transaction.domain.ScheduleTransaction;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleTransactionRepository {
    ScheduleTransaction save(ScheduleTransaction scheduleTransaction);
    Optional<ScheduleTransaction> findById(long scheduleTransactionId);
    long countBySourceAndScheduleDt(Long sourceAccountId, LocalDate scheduleDt);
}
