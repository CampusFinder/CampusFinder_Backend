package com.example.campusfinder.email.service;

import com.example.campusfinder.email.config.ProfessorEmailConfig;
import com.example.campusfinder.email.dto.EmailRequest;
import com.example.campusfinder.email.repository.EmailVerificationRepository;
import com.example.campusfinder.email.utils.EmailVerificationUtils;
import com.example.campusfinder.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationUtils emailVerificationUtils;
    private final EmailVerificationRepository emailVerificationRepository;
    private final ProfessorEmailConfig professorEmailConfig;

    // 임시로 교수님의 학교 이메일 및 세종대학교로 고정
    private static final String PROFESSOR_EMAIL = "tlswlgns1003@naver.com";
    private static final String UNIVERSITY_NAME = "세종대학교";


    public void sendVerificationCode(EmailRequest emailRequest) throws IOException {
        Role role = emailRequest.role();

        if (role == Role.STUDENT) {
            // 학생 인증 로직
            handleStudentVerification(emailRequest);
        } else if (role == Role.PROFESSOR) {
            // 교수 인증 로직 (임시 구현, 추후 추가 필요)
            handleProfessorVerification(emailRequest);
        }
    }

    //오류 수정 test
    private void handleStudentVerification(EmailRequest emailRequest) throws IOException {
        // 이미 회원가입이 완료된 이메일인 경우
        if (emailVerificationRepository.isRegistered(emailRequest.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 이미 인증된 이메일인 경우
        if (emailVerificationRepository.isEmailVerified(emailRequest.email())) {
            throw new IllegalArgumentException("이미 인증된 이메일입니다.");
        }

        // 새로 이메일 인증 진행
        emailVerificationUtils.clearPendingVerification(emailRequest.email());
        emailVerificationUtils.sendVerificationCode(emailRequest);
    }

    private void handleProfessorVerification(EmailRequest emailRequest) {
        // 학교 이메일 및 학교 이름이 정확히 일치하는 경우만 인증 메일을 보냄
        if (PROFESSOR_EMAIL.equals(emailRequest.email()) && UNIVERSITY_NAME.equals(emailRequest.univName())) {
            emailVerificationUtils.sendProfessorVerificationCode(emailRequest.email());

        } else {
            throw new IllegalArgumentException("등록되지 않은 교수 이메일이거나 학교 정보가 일치하지 않습니다.");
        }
    }

    private boolean isAllowedDomain(String email) {
        // 이메일 도메인 추출
        String domain = email.substring(email.lastIndexOf("@") + 1);
        // 설정된 도메인 리스트에 해당 도메인이 포함되어 있는지 확인
        List<String> allowedDomains = professorEmailConfig.getAllowedDomains();
        return allowedDomains.contains(domain);
    }


    public boolean verifyCode(String email, String univName, int code, Role role) throws IOException {
        boolean isVerified = false;

        if (role == Role.STUDENT) {
            isVerified = emailVerificationUtils.verifyStudentCode(email, univName, code);
        } else if (role == Role.PROFESSOR) {
            isVerified = emailVerificationUtils.verifyProfessorCode(email, code);
        }

        // 이메일 인증이 성공한 경우, 인증 상태를 저장
        if (isVerified) {
            emailVerificationRepository.saveVerifiedEmail(email);  // 인증된 이메일을 저장
        }

        return isVerified;
    }

    public boolean isVerificationPending(String email) {
        return emailVerificationUtils.isVerificationPending(email);
    }


    // 이메일 인증 여부를 체크하는 메서드 추가
    public boolean isEmailVerified(String email) {
        return emailVerificationRepository.isEmailVerified(email);
    }
}
