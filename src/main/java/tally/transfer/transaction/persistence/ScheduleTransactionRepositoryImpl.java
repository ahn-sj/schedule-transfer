package tally.transfer.transaction.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tally.transfer.transaction.domain.ScheduleTransaction;
import tally.transfer.transaction.domain.repository.ScheduleTransactionRepository;
import tally.transfer.transaction.persistence.repository.ScheduleTransactionJpaRepository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleTransactionRepositoryImpl implements ScheduleTransactionRepository {

    private final ScheduleTransactionJpaRepository scheduleTransactionJpaRepository;

    @Override
    public ScheduleTransaction save(final ScheduleTransaction scheduleTransaction) {
        return scheduleTransactionJpaRepository.save(scheduleTransaction);
    }

    @Override
    public Optional<ScheduleTransaction> findById(final long scheduleTransactionId) {
        return scheduleTransactionJpaRepository.findById(scheduleTransactionId);
    }

    @Override
    public long countBySourceAndScheduleDt(final Long sourceAccountId, final LocalDate scheduleDt) {
        return scheduleTransactionJpaRepository.countBySourceAndScheduleDt(sourceAccountId, scheduleDt);
    }
}
