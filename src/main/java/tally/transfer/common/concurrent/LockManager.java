package tally.transfer.common.concurrent;

import java.util.concurrent.TimeUnit;

public interface LockManager {

    boolean acquire(String key, long timeout, TimeUnit unit);
    void release(String key);
    boolean isAcquired(String key);
}
