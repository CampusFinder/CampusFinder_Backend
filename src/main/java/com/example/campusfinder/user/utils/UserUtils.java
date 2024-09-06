package com.example.campusfinder.user.utils;

import com.example.campusfinder.email.repository.EmailVerificationRepository;
import com.example.campusfinder.sms.repository.SmsCertificationRepository;
import com.example.campusfinder.user.dto.request.signup.SignUpRequestDto;
import com.example.campusfinder.user.entity.Role;
import com.example.campusfinder.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final EmailVerificationRepository emailVerificationRepository;
    private final SmsCertificationRepository smsCertificationRepository;

    // 이메일 인증 여부 확인
    public boolean isEmailVerified(String email) {
        return emailVerificationRepository.isEmailVerified(email);
    }

    // 전화번호 인증 여부 확인
    public boolean isPhoneVerified(String phoneNumber) {
        return smsCertificationRepository.isPhoneVerified(phoneNumber);
    }

    public UserEntity createUserEntity(SignUpRequestDto signUpRequest) {
        return UserEntity.builder()
                .email(signUpRequest.email().email())
                .phone(signUpRequest.phone().phoneNumber())
                .password(signUpRequest.password().password())
                .nickname(signUpRequest.nickname().nickname())
                .role(Role.valueOf(signUpRequest.role().role()))
                .emailVerified(true)
                .phoneVerified(true)
                .build();
    }
}
