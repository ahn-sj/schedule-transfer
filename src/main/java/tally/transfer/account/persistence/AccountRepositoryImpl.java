package tally.transfer.account.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tally.transfer.account.domain.Account;
import tally.transfer.account.domain.repository.AccountRepository;
import tally.transfer.account.persistence.repository.AccountJpaRepository;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public Account save(final Account account) {
        return accountJpaRepository.save(account);
    }
}
