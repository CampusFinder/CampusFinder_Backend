package com.example.campusfinder.email.repository;

import com.example.campusfinder.email.utils.RedisEmailStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepository {

    private final RedisEmailStore redisEmailStore;
    private final StringRedisTemplate redisTemplate;

    // 이메일 인증 여부 확인 메서드 추가
    public boolean isEmailVerified(String email) {
        return redisEmailStore.isVerificationCompleted(email);
    }

    // Redis 키 생성 메서드 (회원가입 여부 확인)
    private String getRegistrationKey(String email) {
        return "email:registered:" + email;
    }

    // 회원가입 여부 확인
    public boolean isRegistered(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getRegistrationKey(email)));
    }

    // 회원가입 완료 시 이메일 등록
    public void saveRegisteredEmail(String email) {
        redisTemplate.opsForValue().set(getRegistrationKey(email), "REGISTERED");
    }
}
