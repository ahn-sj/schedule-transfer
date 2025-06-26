package tally.transfer.transaction.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tally.transfer.transaction.domain.ScheduleTransaction;

import java.time.LocalDate;

public interface ScheduleTransactionJpaRepository extends JpaRepository<ScheduleTransaction, Long> {
    long countBySourceAndScheduleDt(Long source, LocalDate scheduleDt);
}
