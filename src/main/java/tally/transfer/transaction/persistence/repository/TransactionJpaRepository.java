package tally.transfer.transaction.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tally.transfer.transaction.domain.Transaction;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
}
