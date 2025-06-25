package tally.transfer.account.domain.repository;

import tally.transfer.account.domain.Account;
import tally.transfer.account.domain.enums.BankCode;

import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findByBankAndAccountNumber(BankCode bank, String accountNumber);
    Account getByBankAndAccountNumber(BankCode bank, String accountNumber);
}
