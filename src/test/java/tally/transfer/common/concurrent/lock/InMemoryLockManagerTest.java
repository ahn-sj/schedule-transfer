package tally.transfer.common.concurrent.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tally.transfer.common.concurrent.LockManager;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryLockManagerTest {

    @Test
    @DisplayName("[성공] 락 획득 성공")
    void lock_is_acquired_successfully() {
        // given:
        final LockManager lockManager = new InMemoryLockManager();
        final String key = "123:lock";

        // when:
        final boolean acquired = lockManager.acquire(key, 3, TimeUnit.SECONDS);

        // then:
        assertThat(acquired).isTrue();
        assertThat(lockManager.isAcquired(key)).isTrue();
    }

}