package com.example.campusfinder.sms.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@RequiredArgsConstructor
@Repository
public class SmsCertificationRepository {

    private final StringRedisTemplate redisTemplate;

    // Redis 키 생성 메서드 (휴대폰 인증 여부 확인)
    private String getVerificationKey(String phoneNumber) {
        return "sms:verification:" + phoneNumber;
    }

    // Redis 키 생성 메서드 (회원가입 여부 확인)
    private String getRegistrationKey(String phoneNumber) {
        return "sms:registered:" + phoneNumber;
    }

    // 휴대폰 인증 여부 확인
    public boolean isPhoneVerified(String phoneNumber) {
        String verificationStatus = redisTemplate.opsForValue().get(getVerificationKey(phoneNumber));
        return "VERIFIED".equals(verificationStatus);
    }

    // 휴대폰 인증 성공 시 상태 저장
    public void markPhoneVerified(String phoneNumber) {
        redisTemplate.opsForValue().set(getVerificationKey(phoneNumber), "VERIFIED");
    }

    // 회원가입 여부 확인
    public boolean isRegistered(String phoneNumber) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getRegistrationKey(phoneNumber)));
    }

    // 회원가입 완료 시 휴대폰 번호 등록
    public void saveRegisteredPhoneNumber(String phoneNumber) {
        redisTemplate.opsForValue().set(getRegistrationKey(phoneNumber), "REGISTERED", Duration.ofDays(1)); // 1일 동안 유지
    }

    // 인증 정보 삭제
    public void clearVerificationState(String phoneNumber) {
        redisTemplate.delete(getVerificationKey(phoneNumber));
    }
}
