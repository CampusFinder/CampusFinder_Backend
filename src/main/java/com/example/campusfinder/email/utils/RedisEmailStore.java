package com.example.campusfinder.email.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisEmailStore {
    private final StringRedisTemplate redisTemplate;

    // 기존의 인증 상태만 저장하는 메서드
    public void savePendingState(String email, long expirationTime) {
        String redisKey = "email:verification:" + email;
        redisTemplate.opsForValue().set(redisKey, "PENDING", Duration.ofSeconds(expirationTime));
    }

    // 새롭게 추가된 메서드: 인증번호를 저장하는 기능 포함
    public void savePendingState(String email, String verificationCode, long expirationTime) {
        String redisKey = "email:verification:" + email;

        // 인증번호와 상태를 JSON 형식으로 저장할 수 있습니다. (Optional)
        String value = String.format("{\"status\":\"PENDING\",\"code\":\"%s\"}", verificationCode);

        // Redis에 인증번호와 상태를 저장
        redisTemplate.opsForValue().set(redisKey, value, Duration.ofSeconds(expirationTime));
    }

    public void saveCompletedState(String email) {
        String redisKey = "email:verification:" + email;
        redisTemplate.opsForValue().set(redisKey, "COMPLETED", Duration.ofMinutes(10));
    }

    public boolean isVerificationPending(String email) {
        String redisKey = "email:verification:" + email;
        String value = redisTemplate.opsForValue().get(redisKey);

        // JSON 형식일 경우, PENDING 상태만 확인
        if (value != null && value.contains("\"status\":\"PENDING\"")) {
            return true;
        }

        // 이전 버전의 단순 PENDING 확인 로직
        return value != null && "PENDING".equals(value);
    }

    public boolean isVerificationCompleted(String email) {
        String redisKey = "email:verification:" + email;
        String value = redisTemplate.opsForValue().get(redisKey);

        return value != null && "COMPLETED".equals(value);
    }

    public void clearPendingVerification(String email) {
        String redisKey = "email:verification:" + email;
        redisTemplate.delete(redisKey);
    }
}
