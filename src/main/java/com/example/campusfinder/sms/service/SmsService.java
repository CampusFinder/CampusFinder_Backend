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
    private final SmsCertificationRepository smsCertificationRepository;
    private final EmailVerificationRepository emailVerificationRepository;  // 이메일 인증 상태 확인을 위한 의존성 추가


    @Transactional
    public void sendSms(SmsRequest request) {
        String phoneNumber = request.phoneNum();
        String certificationNumber = generateRandomCode();

        // 이메일 인증이 완료되지 않은 경우, SMS 인증 불가
        if (!emailVerificationRepository.isEmailVerified(request.email())) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        smsSender.sendSms(phoneNumber, String.format("[CampusFinder] 인증번호는 %s 입니다.", certificationNumber));
        smsCertificationRepository.createSmsCertification(phoneNumber, certificationNumber);
    }

    // 6자리 인증번호 생성
    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        int randomNumber = 100000 + random.nextInt(900000); // 100000부터 999999까지
        return String.valueOf(randomNumber);
    }

    // 인증번호 검증
    public void verifySms(SmsRequest request) {
        if (!isValidCertification(request)) {
            throw new IllegalArgumentException("인증번호가 올바르지 않습니다.");
        }
        smsCertificationRepository.verifyPhone(request.phoneNum());
        //smsCertificationRepository.removeSmsCertification(request.phoneNum());
    }

    // 인증번호 검증 로직
    private boolean isValidCertification(SmsRequest request) {
        return smsCertificationRepository.hasKey(request.phoneNum()) &&
                smsCertificationRepository.getSmsCertification(request.phoneNum())
                        .equals(request.code());
    }
}