package com.example.campusfinder.email.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisEmailStore {

    private final StringRedisTemplate redisTemplate;

    // Redis 키 생성 메서드
    private String getRedisKey(String email) {
        return "email:verification:" + email;
    }

    // 인증번호와 PENDING 상태 저장
    public void savePendingState(String email, String verificationCode, long expirationTime) {
        String redisKey = "email:verification:" + email;
        String value = String.format("{\"status\":\"PENDING\",\"code\":\"%s\"}", verificationCode);
        redisTemplate.opsForValue().set(redisKey, value, Duration.ofSeconds(expirationTime));
    }

    // 인증번호 없이 PENDING 상태 저장
    public void savePendingState(String email, long expirationTime) {
        String redisKey = "email:verification:" + email;
        redisTemplate.opsForValue().set(redisKey, "{\"status\":\"PENDING\"}", Duration.ofSeconds(expirationTime));
    }
    // 인증 완료 시 상태를 JSON 형식으로 저장
    public void saveCompletedState(String email) {
        String redisKey = getRedisKey(email);
        String value = "{\"status\":\"COMPLETED\"}";
        redisTemplate.opsForValue().set(redisKey, value, Duration.ofMinutes(10));  // 인증 후 10분간 유지
    }

    // 인증 상태가 PENDING인지 확인
    public boolean isVerificationPending(String email) {
        String redisKey = getRedisKey(email);
        String value = redisTemplate.opsForValue().get(redisKey);
        return value != null && value.contains("\"status\":\"PENDING\"");
    }

    // 인증 상태가 COMPLETED인지 확인
    public boolean isVerificationCompleted(String email) {
        String redisKey = getRedisKey(email);
        String value = redisTemplate.opsForValue().get(redisKey);
        return value != null && value.contains("\"status\":\"COMPLETED\"");
    }

    // Redis에서 저장된 인증번호 가져오기
    public String getVerificationCode(String email) {
        String redisKey = getRedisKey(email);
        String value = redisTemplate.opsForValue().get(redisKey);
        if (value != null && value.contains("\"code\":\"")) {
            int start = value.indexOf("\"code\":\"") + 8;
            int end = value.indexOf("\"", start);
            return value.substring(start, end);
        }
        return null;
    }

    // 인증 정보 삭제
    public void clearVerificationState(String email) {
        String redisKey = getRedisKey(email);
        redisTemplate.delete(redisKey);
    }
}
