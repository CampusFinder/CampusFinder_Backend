package com.example.campusfinder.email.service;

import com.example.campusfinder.email.dto.EmailRequest;
import com.example.campusfinder.email.repository.EmailVerificationRepository;
import com.example.campusfinder.email.utils.EmailVerificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationUtils emailVerificationUtils;
    private final EmailVerificationRepository emailVerificationRepository;

    public void sendVerificationCode(EmailRequest emailRequest) throws IOException {
        // 회원가입이 완료된 사용자는 재인증 불가, 그러나 회원가입 전 상태에서는 재인증 가능
        if (emailVerificationRepository.isEmailVerified(emailRequest.email())) {
            // 회원가입이 완료되지 않은 경우에만 다시 인증 가능
            if (!emailVerificationRepository.isRegistered(emailRequest.email())) {
                // 기존 인증 상태 클리어 후, 새로운 인증 코드를 발송
                emailVerificationUtils.clearPendingVerification(emailRequest.email());
                emailVerificationUtils.sendVerificationCode(emailRequest);
            } else {
                throw new IllegalArgumentException("이미 인증된 이메일입니다.");
            }
        } else {
            // 기존 인증이 없거나 PENDING 상태일 경우 새 인증코드를 발송
            emailVerificationUtils.clearPendingVerification(emailRequest.email());
            emailVerificationUtils.sendVerificationCode(emailRequest);
        }
    }

    public boolean verifyCode(String email, String univName, int code) throws IOException {
        // 인증이 만료되었거나 존재하지 않는 경우
        if (emailVerificationUtils.isVerificationExpired(email)) {
            throw new IllegalArgumentException("인증 요청이 유효하지 않습니다. 제한 시간이 지났거나 요청이 만료되었습니다.");
        }

        // UnivCert API로 인증 코드 검증
        boolean isVerified = emailVerificationUtils.verifyCode(email, univName, code);

        if (isVerified) {
            // 인증 성공 시, 이메일 인증 상태 저장
            emailVerificationRepository.saveVerifiedEmail(email);
        } else {
            throw new IllegalArgumentException("인증 코드가 유효하지 않습니다.");
        }

        return isVerified;
    }

    public boolean isVerificationPending(String email) {
        return emailVerificationUtils.isVerificationPending(email);
    }
}
