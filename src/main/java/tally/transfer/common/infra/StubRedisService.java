package tally.transfer.common.infra;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class StubRedisService implements RedisService {

    private final Map<String, AtomicLong> store = new ConcurrentHashMap<>();
    private final Map<String, Long> ttlStore = new ConcurrentHashMap<>();

    @Override
    public Long increment(final String key) {
        return store.computeIfAbsent(key, k -> new AtomicLong(0)).incrementAndGet();
    }

    @Override
    public Long decrement(final String key) {
        return store.computeIfAbsent(key, k -> new AtomicLong(0)).decrementAndGet();
    }

    @Override
    public String getValue(final String key) {
        AtomicLong value = store.get(key);
        return value == null ? null : String.valueOf(value.get());
    }

    @Override
    public void expire(final String key, final Duration ttl) {
        ttlStore.put(key, ttl.toSeconds());
    }
}
