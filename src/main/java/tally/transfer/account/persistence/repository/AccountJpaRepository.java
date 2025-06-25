package tally.transfer.account.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tally.transfer.account.domain.Account;
import tally.transfer.account.domain.enums.BankCode;

import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByBankCodeAndAccountNumber(BankCode bank, String accountNumber);
}
