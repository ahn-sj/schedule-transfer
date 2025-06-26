package tally.transfer.common.concurrent.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tally.transfer.common.concurrent.LockManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static tally.transfer.util.ThreadUtils.delay;

@Slf4j
@Component
public class InMemoryLockManager implements LockManager {

    private static final long AUTO_RELEASE_SECONDS = 3L;
    private static final int MAX_RETRY_ATTEMPTS = 3;

    private final Map<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    /**
     * 락을 획득합니다.
     * - 최대 3번(default)의 시도를 하며, 각 시도 사이에 지연을 둡니다.
     *
     * @param key 락을 획득할 키
     * @param timeout 락 획득 대기 시간
     * @param unit 락 획득 대기 시간 단위
     * @return 락 획득 성공 여부
     */
    @Override
    public boolean acquire(
            final String key,
            final long timeout,
            final TimeUnit unit
    ) {
        long ttl = calculateTtl(timeout, unit);

        for (int attempt = 1; attempt <= MAX_RETRY_ATTEMPTS; attempt++) {
            try {
                log.debug("[InMemory] 락 획득 시도: Attempt {}, key {}", attempt, key);
                final ReentrantLock lock = locks.computeIfAbsent(key, k -> new ReentrantLock(true));
                boolean acquired = lock.tryLock(ttl, TimeUnit.SECONDS);

                if (acquired) {
                    log.debug("Lock 획득 성공 (시도 횟수: {}): key={}", attempt, key);
                    return true;
                }
                if(attempt < MAX_RETRY_ATTEMPTS) {
                    final long interval = ThreadLocalRandom.current().nextLong(attempt, attempt * 2);
                    delay(interval);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        log.warn("Lock 획득 실패: key={}, attempts={}", key, MAX_RETRY_ATTEMPTS);

        return false;
    }

    /**
     * TTL을 계산합니다.
     * - 0L: 무제한 대기 {{ TODO: 무제한 대기 기능 구현 필요 }}
     * - 음수: 자동 해제 시간 (AUTO_RELEASE_SECONDS)
     * - 양수: 주어진 시간 단위로 변환하여 최소 1초 이상으로 설정
     *
     * @param timeout lock 획득 대기 시간
     * @param unit lock 획득 대기 시간 단위
     * @return 계산된 TTL (초 단위)
     */
    private long calculateTtl(final long timeout, final TimeUnit unit) {
        if (timeout == 0L) {
            return 0L;
        } else if (timeout < 0L) {
            return AUTO_RELEASE_SECONDS;
        } else {
            return Math.max(1L, unit.toSeconds(timeout));
        }
    }

    /**
     * 락을 해제합니다.
     * - 현재 스레드가 락을 보유하고 있는 경우에만 해제합니다.
     * - 락이 해제된 후, 대기 중인 스레드가 없고 락이 잠겨 있지 않으면 해당 키를 맵에서 제거합니다. (메모리 누수 방지)
     *
     * @param key 락을 해제할 키
     */
    @Override
    public void release(final String key) {
        final ReentrantLock lock = locks.get(key);

        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
            log.info("Lock 해제 성공: key={}", key);

            if (!lock.hasQueuedThreads() && !lock.isLocked()) { // 락이 완전히 해제되고 대기자가 없으면 Map에서 제거
                locks.remove(key);
                log.info("Lock 객체 제거: key={}", key);
            }
        }
    }

    /**
     * 락이 획득되었는지 확인합니다.
     * - 락이 존재하고 현재 스레드가 락을 보유하고 있는 경우 true를 반환합니다.
     *
     * @param key 락을 확인할 키
     * @return
     */
    @Override
    public boolean isAcquired(final String key) {
        return locks.containsKey(key) && locks.get(key).isLocked();
    }
}
