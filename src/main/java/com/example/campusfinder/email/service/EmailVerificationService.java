package com.example.campusfinder.email.service;

import com.example.campusfinder.email.dto.EmailRequest;
import com.example.campusfinder.email.repository.EmailVerificationRepository;
import com.example.campusfinder.email.utils.EmailVerificationUtils;
import com.example.campusfinder.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationUtils emailVerificationUtils;
    private final EmailVerificationRepository emailVerificationRepository;

    public void sendVerificationCode(EmailRequest emailRequest) throws IOException {
        Role role = emailRequest.role();

        if (role == Role.ROLE_STUDENT) {
            // 학생 인증 로직
            handleStudentVerification(emailRequest);
        } else if (role == Role.ROLE_PROFESSOR) {
            // 교수 인증 로직 (임시 구현, 추후 추가 필요)
            handleProfessorVerification(emailRequest);
        }
    }

    private void handleStudentVerification(EmailRequest emailRequest) throws IOException {
        if (emailVerificationRepository.isEmailVerified(emailRequest.email())) {
            if (!emailVerificationRepository.isRegistered(emailRequest.email())) {
                emailVerificationUtils.clearPendingVerification(emailRequest.email());
                emailVerificationUtils.sendVerificationCode(emailRequest);
            } else {
                throw new IllegalArgumentException("이미 인증된 이메일입니다.");
            }
        } else {
            emailVerificationUtils.clearPendingVerification(emailRequest.email());
            emailVerificationUtils.sendVerificationCode(emailRequest);
        }
    }

    private void handleProfessorVerification(EmailRequest emailRequest) {
        // 교수 인증 로직 (임시)
        if (emailVerificationRepository.isRegistered(emailRequest.email())) {
            // 교수 이메일로 인증 코드 발송 로직 추가
            emailVerificationUtils.sendProfessorVerificationCode(emailRequest.email());
        } else {
            throw new IllegalArgumentException("등록되지 않은 교수 이메일입니다.");
        }
    }

    public boolean verifyCode(String email, String univName, int code, Role role) throws IOException {
        if (role == Role.ROLE_STUDENT) {
            return emailVerificationUtils.verifyStudentCode(email, univName, code);
        } else if (role == Role.ROLE_PROFESSOR) {
            return emailVerificationUtils.verifyProfessorCode(email, code);
        }
        return false;
    }

    public boolean isVerificationPending(String email) {
        return emailVerificationUtils.isVerificationPending(email);
    }
}
