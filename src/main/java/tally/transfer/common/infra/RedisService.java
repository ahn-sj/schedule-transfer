package tally.transfer.common.infra;

import java.time.Duration;

public interface RedisService {

    Long increment(String key);

    Long decrement(String key);

    String getValue(String key);

    void expire(String key, Duration ttl);
}
