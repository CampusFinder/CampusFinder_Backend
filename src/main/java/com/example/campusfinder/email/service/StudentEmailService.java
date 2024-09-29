package com.example.campusfinder.email.service;

import com.example.campusfinder.email.dto.EmailRequest;
import com.example.campusfinder.email.repository.EmailVerificationRepository;
import com.example.campusfinder.email.utils.EmailFormatValidator;
import com.example.campusfinder.email.utils.RedisEmailStore;
import com.example.campusfinder.email.utils.UnivCertApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StudentEmailService implements EmailService {

    private final RedisEmailStore redisEmailVerificationStore;
    private final EmailFormatValidator emailFormatValidator;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UnivCertApi univCertApiService;

    @Value("${univcert.api-key}")
    private String univCertApiKey;
    private final static long EXPIRATION_TIME = 600L;

    @Override
    public void sendVerificationCode(EmailRequest emailRequest) throws IOException {
        String email = emailRequest.email();

        // 이메일 형식 검증
        if (!emailFormatValidator.isValid(email)) {
            throw new IllegalArgumentException("이메일 형식을 맞춰주세요.");
        }

        // 이미 회원가입된 이메일 확인
        if (emailVerificationRepository.isRegistered(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // UnivCert API에서 인증 상태 확인
        boolean isApiVerificationCompleted = false;
        try {
            isApiVerificationCompleted = univCertApiService.checkEmailCertificationStatus(email);
        } catch (IOException e) {
            throw new IllegalStateException("API 인증 상태 확인 중 오류가 발생했습니다: " + e.getMessage());
        }

        // 기존 인증 상태 확인 (API와 Redis 모두 확인)
        boolean isVerificationPending = redisEmailVerificationStore.isVerificationPending(email);
        boolean isVerificationCompleted = redisEmailVerificationStore.isVerificationCompleted(email);

        // API에서 인증 완료된 상태이거나 Redis에 PENDING 또는 COMPLETED 상태가 있으면 초기화
        if (isApiVerificationCompleted || isVerificationPending || isVerificationCompleted) {
            try {
                // UnivCert API와 Redis의 상태를 초기화
                univCertApiService.clearCertification(email);  // UnivCert API 상태 초기화
                redisEmailVerificationStore.clearVerificationState(email);  // Redis 상태 초기화
            } catch (IOException e) {
                throw new IllegalStateException("API 인증 상태 초기화 중 오류가 발생했습니다: " + e.getMessage());
            }
        }

        // UnivCert API를 통해 새로운 인증 요청
        try {
            // UnivCert API 호출 (응답을 인증번호로 받아옴)
            String verificationCode = univCertApiService.requestCertification(emailRequest);

            // 인증 요청 성공 시 Redis에 인증번호와 PENDING 상태 저장
            redisEmailVerificationStore.savePendingState(email, verificationCode, EXPIRATION_TIME);

        } catch (IllegalStateException e) {
            throw new IllegalStateException("학생 이메일 인증 요청 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    @Override
    public boolean verifyCode(String email, String univName, int code) throws IOException {
        // Redis에서 인증 상태 확인
        if (!redisEmailVerificationStore.isVerificationPending(email)) {
            throw new IllegalArgumentException("인증 코드가 만료되었거나 존재하지 않습니다. 다시 인증 요청을 해주세요.");
        }

        // UnivCert API로 인증 코드 검증
        boolean isVerified = univCertApiService.verifyCertificationCode(email, univName, code, univCertApiKey);

        if (isVerified) {
            // 인증 성공 시 상태를 COMPLETED로 저장
            redisEmailVerificationStore.saveCompletedState(email);
        } else {
            // 인증 실패 시 상태 초기화
            redisEmailVerificationStore.clearVerificationState(email);
            throw new IllegalArgumentException("인증 코드가 유효하지 않습니다. 다시 인증 요청을 해주세요.");
        }

        return isVerified;
    }

    @Override
    public void clearPendingVerification(String email) throws IOException {
        // UnivCert API와 Redis 초기화
        univCertApiService.clearCertification(email);
        redisEmailVerificationStore.clearVerificationState(email);
    }

    @Override
    public boolean isVerificationPending(String email) {
        return redisEmailVerificationStore.isVerificationPending(email);
    }

    @Override
    public boolean isValidEmailFormat(String email) {
        return emailFormatValidator.isValid(email);
    }
}