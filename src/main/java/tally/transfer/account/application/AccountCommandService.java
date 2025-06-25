package tally.transfer.account.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tally.transfer.account.domain.Account;
import tally.transfer.account.domain.enums.AccountType;
import tally.transfer.account.domain.repository.AccountRepository;
import tally.transfer.account.policy.AccountNumberGenerator;
import tally.transfer.user.domain.repository.UserRepository;

import java.util.Objects;

@Slf4j
@Service
public class AccountCommandService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    private final AccountNumberGenerator accountNumberGenerator;

     public AccountCommandService(
             final AccountRepository accountRepository,
             final UserRepository userRepository,
             final AccountNumberGenerator accountNumberGenerator
     ) {
         this.accountRepository = Objects.requireNonNull(accountRepository);
         this.userRepository = Objects.requireNonNull(userRepository);
         this.accountNumberGenerator = Objects.requireNonNull(accountNumberGenerator);
     }

     public Long createAccount(final Long userId, final AccountType type) {
         userRepository.checkExists(userId);
         final Account account = accountRepository.save(Account.open(userId, type, accountNumberGenerator));

         return account.getId();
     }
}
