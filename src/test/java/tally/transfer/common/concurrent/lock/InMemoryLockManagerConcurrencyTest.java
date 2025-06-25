package tally.transfer.common.concurrent.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import tally.transfer.common.concurrent.LockManager;

class InMemoryLockManagerConcurrencyTest {

    private static final String KEY;
    private static final int THREAD_COUNT;

    static {
        KEY = "123:lock";
        THREAD_COUNT = 10;
    }

    @Test
    @DisplayName("[성공] 동시성 - 락 획득 성공")
    void shouldAcquireOnlyOneThread() throws InterruptedException, ExecutionException {
        // given:
        final LockManager lockManager = new InMemoryLockManager();

        final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
        final CountDownLatch ready = new CountDownLatch(THREAD_COUNT);
        final CountDownLatch start = new CountDownLatch(1);

        // when:
        final List<Future<Boolean>> results = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            Future<Boolean> future = EXECUTOR.submit(() -> {
                ready.countDown();
                start.await();

                final boolean acquired = lockManager.acquire(KEY, 1L, TimeUnit.SECONDS);
                if (acquired) {
                    Thread.sleep(4_000); // 실행 중인 스레드가 락을 유지하도록 잠시 대기
                    lockManager.release(KEY);
                }
                return acquired;
            });
            results.add(future);
        }
        ready.await();
        start.countDown();

        // then:
        int successCount = 0;
        for (Future<Boolean> result : results) {
            if (result.get()) {
                successCount++;
            }
        }
        assertThat(successCount).isEqualTo(1);
        assertThat(lockManager.isAcquired(KEY)).isFalse();

        EXECUTOR.shutdown();
    }

    @Test
    @DisplayName("[성공] 동시성 - 락 획득 성공 (모든 스레드)")
    void shouldAcquireAllThread() throws InterruptedException, ExecutionException {
        // given:
        final LockManager lockManager = new InMemoryLockManager();

        final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
        final CountDownLatch ready = new CountDownLatch(THREAD_COUNT);
        final CountDownLatch start = new CountDownLatch(1);

        // when:
        final List<Future<Boolean>> results = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            Future<Boolean> future = EXECUTOR.submit(() -> {
                ready.countDown();
                start.await();

                final boolean acquired = lockManager.acquire(KEY, 1L, TimeUnit.SECONDS);
                if (acquired) {
                    lockManager.release(KEY);
                }
                return acquired;
            });
            results.add(future);
        }
        ready.await();
        start.countDown();

        // then:
        int successCount = 0;
        for (Future<Boolean> result : results) {
            if (result.get()) {
                successCount++;
            }
        }
        assertThat(successCount).isEqualTo(10);
        assertThat(lockManager.isAcquired(KEY)).isFalse();

        EXECUTOR.shutdown();
    }

}