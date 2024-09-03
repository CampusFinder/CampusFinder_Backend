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
        if (emailVerificationRepository.isEmailVerified(emailRequest.email())) {
            throw new IllegalArgumentException("이미 인증된 이메일입니다.");
        }

        emailVerificationUtils.clearPendingVerification(emailRequest.email());
        emailVerificationUtils.sendVerificationCode(emailRequest);
    }

    public boolean verifyCode(String email, String univName, int code) throws IOException {
        if (emailVerificationUtils.isVerificationExpired(email)) {
            throw new IllegalArgumentException("인증 요청이 유효하지 않습니다. 제한 시간이 지났거나 요청이 만료되었습니다.");
        }

        boolean isVerified = emailVerificationUtils.verifyCode(email, univName, code);

        if (isVerified) {
            emailVerificationRepository.saveVerifiedEmail(email);
        }

        return isVerified;
    }

    public boolean isVerificationPending(String email) {
        return emailVerificationUtils.isVerificationPending(email);
    }
}
