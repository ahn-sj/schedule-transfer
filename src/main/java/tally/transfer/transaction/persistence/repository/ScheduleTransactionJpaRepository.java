package tally.transfer.transaction.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tally.transfer.transaction.domain.ScheduleTransaction;

public interface ScheduleTransactionJpaRepository extends JpaRepository<ScheduleTransaction, Long> {
}
