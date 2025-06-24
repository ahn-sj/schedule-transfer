package tally.transfer.account.domain.repository;

import tally.transfer.account.domain.Account;

public interface AccountRepository {
    Account save(Account account);
}
