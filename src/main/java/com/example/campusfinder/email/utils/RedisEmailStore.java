package com.example.campusfinder.email.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * packageName    : com.example.campusfinder.email.utils
 * fileName       : RedisEmailStore
 * author         : tlswl
 * date           : 2024-09-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-28        tlswl       최초 생성
 */
@Component
@RequiredArgsConstructor
public class RedisEmailStore {
    private final StringRedisTemplate redisTemplate;

    public void savePendingState(String email, long expirationTime) {
        String redisKey = "email:verification:" + email;
        redisTemplate.opsForValue().set(redisKey, "PENDING", Duration.ofSeconds(expirationTime));
    }

    public void saveCompletedState(String email) {
        String redisKey = "email:verification:" + email;
        redisTemplate.opsForValue().set(redisKey, "COMPLETED", Duration.ofMinutes(10));
    }

    public boolean isVerificationPending(String email) {
        String redisKey = "email:verification:" + email;
        return redisTemplate.hasKey(redisKey) && "PENDING".equals(redisTemplate.opsForValue().get(redisKey));
    }

    public void clearPendingVerification(String email) {
        String redisKey = "email:verification:" + email;
        if (redisTemplate.hasKey(redisKey)) {
            redisTemplate.delete(redisKey);
        }
    }
}
