package tally.transfer.account.stub;

import org.springframework.stereotype.Repository;
import tally.transfer.account.domain.Account;
import tally.transfer.account.domain.enums.BankCode;
import tally.transfer.account.domain.repository.AccountRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class StubAccountRepository implements AccountRepository {

    private final Map<Long, Account> store = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(1L);

    @Override
    public Account save(final Account account) {
        final long accountId = this.id.getAndIncrement();
        final Account newAccount = new Account(
                accountId,
                account.getBankCode(),
                account.getAccountNumber(),
                account.getUserId(),
                account.getType(),
                account.getBalance()
        );
        store.put(accountId, newAccount);

        return newAccount;
    }

    @Override
    public Optional<Account> findByBankAndAccountNumber(final BankCode bank, final String accountNumber) {
    return store.values()
                .stream()
                .filter(account -> account.getBankCode().equals(bank) && account.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    @Override
    public Account getByBankAndAccountNumber(final BankCode bank, final String accountNumber) {
        return store.values()
                .stream()
                .filter(account -> account.getBankCode().equals(bank) && account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Account not found for bank: " + bank + " and account number: " + accountNumber));
    }
}
