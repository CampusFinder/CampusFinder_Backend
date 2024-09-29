package com.example.campusfinder.sms.service;

import com.example.campusfinder.sms.dto.SmsRequest;
import com.example.campusfinder.sms.repository.SmsCertificationRepository;
import com.example.campusfinder.email.repository.EmailVerificationRepository;
import com.example.campusfinder.sms.utils.SmsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

/**
 * packageName    : com.example.campusfinder.sms.service
 * fileName       : SmsService
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SmsService {

    private final SmsSender smsSender;
    private final RedisPhoneVerificationStore redisPhoneVerificationStore;
    private final EmailVerificationRepository emailVerificationRepository;

    private final static long EXPIRATION_TIME = 300L;  // 5분 유효 시간 설정

    public void sendSmsVerification(SmsRequest request) {
        // 이메일 인증이 완료되었는지 확인
        if (!emailVerificationRepository.isEmailVerified(request.email())) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        // 이미 인증된 핸드폰 번호인지 확인
        if (redisPhoneVerificationStore.isPhoneVerified(request.phoneNum())) {
            throw new IllegalArgumentException("이미 인증된 핸드폰 번호입니다.");
        }

        // 이미 인증된 핸드폰 번호인지 확인
        if (redisPhoneVerificationStore.isPhoneVerified(request.phoneNum())) {
            // 회원가입이 완료되지 않은 경우 인증 상태를 초기화
            redisPhoneVerificationStore.clearVerificationState(request.phoneNum());
        }

        // 새로운 인증 번호 생성 및 저장
        String certificationNumber = generateRandomCode();
        redisPhoneVerificationStore.savePendingState(request.phoneNum(), certificationNumber, EXPIRATION_TIME);

        // SMS 발송
        smsSender.sendSms(request.phoneNum(), String.format("[CampusFinder] 인증번호는 %s 입니다.", certificationNumber));
    }

    // 6자리 인증번호 생성 메서드
    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        int randomNumber = 100000 + random.nextInt(900000); // 100000부터 999999까지
        return String.valueOf(randomNumber);
    }
}