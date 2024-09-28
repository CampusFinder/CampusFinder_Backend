package com.example.campusfinder.email.service;

import com.example.campusfinder.email.dto.EmailRequest;
import com.example.campusfinder.email.repository.EmailVerificationRepository;
import com.example.campusfinder.email.utils.EmailFormatValidator;
import com.example.campusfinder.email.utils.EmailSender;
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
    private final EmailSender emailSender;

    @Value("${univcert.api.key}")
    private String univCertApiKey;
    private final static long EXPIRATION_TIME = 600L;

    @Override
    public void sendVerificationCode(EmailRequest emailRequest) throws IOException {
        // 이메일 형식 검증
        if (!emailFormatValidator.isValid(emailRequest.email())) {
            throw new IllegalArgumentException("이메일 형식을 맞춰주세요.");
        }

        // 이미 회원가입된 이메일 확인
        if (emailVerificationRepository.isRegistered(emailRequest.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 기존 인증 상태 초기화
        redisEmailVerificationStore.clearPendingVerification(emailRequest.email());

        // UnivCert API를 통해 새로운 인증 요청
        univCertApiService.requestCertification(emailRequest);

        // Redis에 PENDING 상태 저장
        redisEmailVerificationStore.savePendingState(emailRequest.email(), EXPIRATION_TIME);

        // 이메일 전송
        String subject = "CampusFinder 학생 인증번호";
        String text = "안녕하세요, CampusFinder입니다.\n\n" + "학생님의 인증번호가 발송되었습니다. 5분 이내에 인증을 완료해주세요.";
        emailSender.sendEmail(emailRequest.email(), subject, text);
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
            redisEmailVerificationStore.clearPendingVerification(email);
            throw new IllegalArgumentException("인증 코드가 유효하지 않습니다. 다시 인증 요청을 해주세요.");
        }

        return isVerified;
    }

    @Override
    public void clearPendingVerification(String email) throws IOException {
        // UnivCert API와 Redis 초기화
        univCertApiService.clearCertification(email);
        redisEmailVerificationStore.clearPendingVerification(email);
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
