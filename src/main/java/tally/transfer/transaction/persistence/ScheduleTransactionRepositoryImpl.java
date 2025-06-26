package tally.transfer.transaction.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tally.transfer.transaction.domain.ScheduleTransaction;
import tally.transfer.transaction.domain.repository.ScheduleTransactionRepository;
import tally.transfer.transaction.persistence.repository.ScheduleTransactionJpaRepository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class ScheduleTransactionRepositoryImpl implements ScheduleTransactionRepository {

    private final ScheduleTransactionJpaRepository scheduleTransactionJpaRepository;

    @Override
    public ScheduleTransaction save(final ScheduleTransaction scheduleTransaction) {
        return scheduleTransactionJpaRepository.save(scheduleTransaction);
    }

    @Override
    public long countBySourceAndScheduleDt(final Long sourceAccountId, final LocalDate scheduleDt) {
        return scheduleTransactionJpaRepository.countBySourceAndScheduleDt(sourceAccountId, scheduleDt);
    }
}
