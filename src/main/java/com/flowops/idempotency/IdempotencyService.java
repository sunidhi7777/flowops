package com.flowops.idempotency;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class IdempotencyService {

    private final StringRedisTemplate redisTemplate;

    public IdempotencyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final Duration KEY_TTL = Duration.ofHours(24);

    public boolean isFirstUse(String idempotencyKey) {
        String redisKey = "idempotency:" + idempotencyKey;
        Boolean wasSet = redisTemplate.opsForValue()
                .setIfAbsent(redisKey, "processed", KEY_TTL);
        return Boolean.TRUE.equals(wasSet);
    }
}