package tally.transfer.transaction.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tally.transfer.account.domain.Account;
import tally.transfer.account.domain.AccountFixture;
import tally.transfer.account.domain.enums.BankCode;
import tally.transfer.account.domain.repository.AccountRepository;
import tally.transfer.account.domain.vo.Money;
import tally.transfer.account.stub.StubAccountRepository;
import tally.transfer.common.concurrent.lock.InMemoryLockManager;
import tally.transfer.common.stub.StubRedisService;
import tally.transfer.common.stub.StubDateProvider;
import tally.transfer.transaction.domain.ScheduleTransaction;
import tally.transfer.transaction.domain.policy.impl.BasicGradeAmountPolicy;
import tally.transfer.transaction.domain.policy.impl.DefaultScheduleDatePolicy;
import tally.transfer.transaction.domain.policy.impl.FixedMaintenanceChecker;
import tally.transfer.transaction.presentation.dto.request.ScheduleTransactionRequest;
import tally.transfer.transaction.stub.StubScheduleTransactionRepository;
import tally.transfer.user.domain.User;
import tally.transfer.user.domain.UserFixture;
import tally.transfer.user.domain.enums.UserGrade;
import tally.transfer.user.domain.repository.UserRepository;
import tally.transfer.user.stub.StubUserRepository;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionCommandServiceTest {

    @Test
    @DisplayName("[성공] 예약 이체를 요청한다.")
    void scheduleTransaction() {
        // given:
        final UserRepository stubUserRepository = new StubUserRepository();
        final User user = UserFixture.aUser().build();
        stubUserRepository.save(user);

        final AccountRepository stubAccountRepository = new StubAccountRepository();
        final Account sender = AccountFixture.anAccount()
                .withUserId(1L)
                .withBankCode(BankCode.TOSS_BANK)
                .withAccountNumber("1234567890")
                .withBalance(Money.wons(100_000))
                .build();
        final Account receiver = AccountFixture.anAccount()
                .withUserId(2L)
                .withBankCode(BankCode.KAKAO_BANK)
                .withAccountNumber("0987654321")
                .withBalance(Money.wons(50_000))
                .build();
        stubAccountRepository.save(sender);
        stubAccountRepository.save(receiver);

        final StubScheduleTransactionRepository stubScheduleTransactionRepository = new StubScheduleTransactionRepository();
        final TransactionCommandService sut = new TransactionCommandService(
                stubScheduleTransactionRepository,
                stubAccountRepository,
                stubUserRepository,
                new FixedMaintenanceChecker(),
                new DefaultScheduleDatePolicy(new StubDateProvider()),
                new InMemoryLockManager(),
                new StubRedisService(),
                Map.of(UserGrade.BASIC, new BasicGradeAmountPolicy())
        );

        final ScheduleTransactionRequest request = new ScheduleTransactionRequest(
                BankCode.TOSS_BANK, "1234567890",
                BankCode.KAKAO_BANK, "0987654321",
                10_000L,
                LocalDate.of(2099, 10, 1),
                null
        );

        // when:
        sut.scheduleTransaction(request);

        // then: 이체 정보가 저장되며 계좌 잔액은 변하지 않는다 (예약 전송이기 때문)
        final Account updatedSender = stubAccountRepository.getByBankAndAccountNumber(BankCode.TOSS_BANK, "1234567890");
        final Account updatedReceiver = stubAccountRepository.getByBankAndAccountNumber(BankCode.KAKAO_BANK, "0987654321");
        final ScheduleTransaction scheduleTransaction = stubScheduleTransactionRepository.findById(1L).get();

        assertEquals(Money.wons(100_000), updatedSender.getBalance());
        assertEquals(Money.wons(50_000), updatedReceiver.getBalance());
        assertEquals(1L, scheduleTransaction.getId());
        assertEquals(BankCode.TOSS_BANK, scheduleTransaction.getSourceBank());
        assertEquals(1L, scheduleTransaction.getSource());
        assertEquals(BankCode.KAKAO_BANK, scheduleTransaction.getDestinationBank());
        assertEquals(2L, scheduleTransaction.getDestination());
        assertEquals(Money.wons(10_000), scheduleTransaction.getAmount());
    }

}
