package com.example.campusfinder.sms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * packageName    : com.example.campusfinder.sms.service
 * fileName       : RedisPhoneVerificationStore
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
public class RedisPhoneVerificationStore {

    private final StringRedisTemplate redisTemplate;

    // 인증 상태 저장
    public void savePendingState(String phoneNumber, String certificationNumber, long expirationTime) {
        String redisKey = getRedisKey(phoneNumber);
        redisTemplate.opsForValue().set(redisKey, certificationNumber, Duration.ofSeconds(expirationTime));
    }

    // 인증 성공 시 VERIFIED 상태로 저장
    public void markPhoneVerified(String phoneNumber) {
        String redisKey = getRedisKey(phoneNumber);
        redisTemplate.opsForValue().set(redisKey, "VERIFIED", Duration.ofMinutes(10));  // 인증 후 10분간 상태 유지
    }

    // Redis에서 인증 상태 확인
    public boolean isPhoneVerified(String phoneNumber) {
        String redisKey = getRedisKey(phoneNumber);
        return "VERIFIED".equals(redisTemplate.opsForValue().get(redisKey));
    }

    // 인증번호가 유효한지 확인
    public boolean isValidCertification(String phoneNumber, String certificationNumber) {
        String redisKey = getRedisKey(phoneNumber);
        return certificationNumber.equals(redisTemplate.opsForValue().get(redisKey));
    }

    // Redis 키 생성 메서드
    private String getRedisKey(String phoneNumber) {
        return "sms:verification:" + phoneNumber;
    }
}

