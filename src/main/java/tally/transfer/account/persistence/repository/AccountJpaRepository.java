package tally.transfer.account.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tally.transfer.account.domain.Account;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
}
