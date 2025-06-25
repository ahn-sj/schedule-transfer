package tally.transfer.account.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tally.transfer.account.domain.Account;
import tally.transfer.account.domain.enums.BankCode;
import tally.transfer.account.domain.exception.AccountException;
import tally.transfer.account.domain.repository.AccountRepository;
import tally.transfer.account.persistence.repository.AccountJpaRepository;
import tally.transfer.common.exception.ErrorCode;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public Account save(final Account account) {
        return accountJpaRepository.save(account);
    }

    @Override
    public Optional<Account> findByBankAndAccountNumber(final BankCode bank, final String accountNumber) {
        return accountJpaRepository.findByBankCodeAndAccountNumber(bank, accountNumber);
    }

    @Override
    public Account getByBankAndAccountNumber(final BankCode bank, final String accountNumber) {
        return accountJpaRepository.findByBankCodeAndAccountNumber(bank, accountNumber)
                .orElseThrow(() -> new AccountException.AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND_ERROR, "계좌를 찾을 수 없습니다. 은행: " + bank + ", 계좌번호: " + accountNumber));
    }
}
