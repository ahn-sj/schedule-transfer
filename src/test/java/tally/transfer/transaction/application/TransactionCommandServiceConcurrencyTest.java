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
import tally.transfer.common.infra.StubRedisService;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionCommandServiceConcurrencyTest {

    @Test
    @DisplayName("[성공] 동시성 - 10개의 예약 이체 요청이 동시에 와도 전부 성공한다.")
    void allTenConcurrentRequestsShouldSucceed() throws InterruptedException {
        // given:
        TransactionCommandService service = createStubService();
        ScheduleTransactionRequest request = new ScheduleTransactionRequest(
                BankCode.TOSS_BANK, "1234567890",
                BankCode.KAKAO_BANK, "0987654321",
                10_000L,
                LocalDate.of(2099, 10, 1),
                null
        );
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        AtomicInteger successCount = new AtomicInteger();

        // when:
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    service.scheduleTransaction(request);
                    successCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();

        // then:
        assertEquals(10, successCount.get());
    }

    @Test
    @DisplayName("[성공] 동시성 - 10개 예약이 등록된 후 추가 요청은 실패한다.")
    void fiveAdditionalRequestsShouldBeRejectedAfterTenAlreadySaved() throws InterruptedException {
        // given:
        final TransactionCommandService service = createStubService();
        final ScheduleTransactionRequest request = new ScheduleTransactionRequest(
                BankCode.TOSS_BANK, "1234567890",
                BankCode.KAKAO_BANK, "0987654321",
                10_000L,
                LocalDate.of(2099, 10, 1),
                null
        );

        // 사전 등록 10건
        for (int i = 0; i < 10; i++) {
            service.scheduleTransaction(request);
        }

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when:
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    service.scheduleTransaction(request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdown();

        // then:
        assertEquals(0, successCount.get());
        assertEquals(5, failCount.get());
    }

    @Test
    @DisplayName("[성공] 동시성 - 10번째 슬롯을 10개 스레드가 경쟁해도 한 개의 요청만 성공해야 한다.")
    void onlyOneRequestShouldSucceedWhenTwoCompeteForTenthSlot() throws InterruptedException {
        // given:
        final TransactionCommandService service = createStubService();
        final ScheduleTransactionRequest request = new ScheduleTransactionRequest(
                BankCode.TOSS_BANK, "1234567890",
                BankCode.KAKAO_BANK, "0987654321",
                10_000L,
                LocalDate.of(2099, 10, 1),
                null
        );

        // 사전 9건 등록
        for (int i = 0; i < 9; i++) {
            service.scheduleTransaction(request);
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    service.scheduleTransaction(request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // then:
        assertEquals(1, successCount.get());
        assertEquals(9, failCount.get());
    }

    private TransactionCommandService createStubService() {
        StubRedisService stubRedisService = new StubRedisService();
        final UserRepository stubUserRepository = new StubUserRepository();
        final User user = UserFixture.aUser().withId(1L).build();
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

        return new TransactionCommandService(
                new StubScheduleTransactionRepository(),
                stubAccountRepository,
                stubUserRepository,
                new FixedMaintenanceChecker(),
                new DefaultScheduleDatePolicy(() -> LocalDate.of(2099, 10, 1)),
                new InMemoryLockManager(),
                stubRedisService,
                Map.of(UserGrade.BASIC, new BasicGradeAmountPolicy())
        );
    }
}
