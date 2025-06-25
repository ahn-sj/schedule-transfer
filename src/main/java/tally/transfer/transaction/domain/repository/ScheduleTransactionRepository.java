package tally.transfer.transaction.domain.repository;

import tally.transfer.transaction.domain.ScheduleTransaction;

public interface ScheduleTransactionRepository {
    ScheduleTransaction save(ScheduleTransaction scheduleTransaction);
}
