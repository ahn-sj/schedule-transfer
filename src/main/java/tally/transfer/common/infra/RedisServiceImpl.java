package tally.transfer.common.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long increment(final String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    @Override
    public Long decrement(final String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    @Override
    public String getValue(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void expire(final String key, final Duration ttl) {
        redisTemplate.expire(key, ttl);
    }
}
