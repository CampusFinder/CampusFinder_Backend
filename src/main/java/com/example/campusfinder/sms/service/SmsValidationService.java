package com.example.campusfinder.sms.service;

import com.example.campusfinder.sms.dto.SmsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * packageName    : com.example.campusfinder.sms.service
 * fileName       : SmsValidationService
 * author         : tlswl
 * date           : 2024-09-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-28        tlswl       최초 생성
 */
@Service
@RequiredArgsConstructor
public class SmsValidationService {

    private final RedisPhoneVerificationStore redisPhoneVerificationStore;

    public void verifySms(SmsRequest request) {
        // Redis에 저장된 인증번호가 유효한지 확인
        if (!redisPhoneVerificationStore.isValidCertification(request.phoneNum(), request.code())) {
            throw new IllegalArgumentException("인증번호가 올바르지 않습니다.");
        }
        // 인증 성공 시 상태를 VERIFIED로 변경
        redisPhoneVerificationStore.markPhoneVerified(request.phoneNum());
    }
}
