package tally.transfer.transaction.stub;

import org.springframework.stereotype.Repository;
import tally.transfer.transaction.domain.ScheduleTransaction;
import tally.transfer.transaction.domain.repository.ScheduleTransactionRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class StubScheduleTransactionRepository implements ScheduleTransactionRepository {

    private final Map<Long, ScheduleTransaction> store = new HashMap<>();
    private final AtomicLong id = new AtomicLong(1L);

    @Override
    public ScheduleTransaction save(final ScheduleTransaction scheduleTransaction) {
        final long transactionId = this.id.getAndIncrement();
        final ScheduleTransaction newTransaction = new ScheduleTransaction(
                transactionId,
                scheduleTransaction.getSourceBank(),
                scheduleTransaction.getSource(),
                scheduleTransaction.getDestinationBank(),
                scheduleTransaction.getDestination(),
                scheduleTransaction.getAmount(),
                scheduleTransaction.getStatus(),
                scheduleTransaction.getFailureReason(),
                scheduleTransaction.getScheduleDt(),
                scheduleTransaction.getReservedAt(),
                scheduleTransaction.getExecutedAt(),
                scheduleTransaction.getMemo()
        );
        store.put(transactionId, newTransaction);

        return newTransaction;
    }

    @Override
    public Optional<ScheduleTransaction> findById(final long scheduleTransactionId) {
        return Optional.ofNullable(store.get(scheduleTransactionId));
    }

    @Override
    public long countBySourceAndScheduleDt(final Long sourceAccountId, final LocalDate scheduleDt) {
        return store.values()
                .stream()
                .filter(transaction ->
                        sourceAccountId.equals(transaction.getSource())
                                && scheduleDt.equals(transaction.getScheduleDt()))
                .count();
    }
}
