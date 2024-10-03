package com.example.campusfinder.user.utils;

import com.example.campusfinder.email.repository.EmailVerificationRepository;
import com.example.campusfinder.sms.repository.SmsCertificationRepository;
import com.example.campusfinder.user.dto.request.signup.SignUpRequestDto;
import com.example.campusfinder.user.entity.Role;
import com.example.campusfinder.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final EmailVerificationRepository emailVerificationRepository;
    private final SmsCertificationRepository smsCertificationRepository;
    private final RedisTemplate redisTemplate;

    // 이메일 인증 여부 확인
    public boolean isEmailVerified(String email) {
        return emailVerificationRepository.isEmailVerified(email);
    }

    // 전화번호 인증 여부 확인
    public boolean isPhoneVerified(String phoneNumber) {
        return smsCertificationRepository.isPhoneVerified(phoneNumber);
    }

    public UserEntity createUserEntity(SignUpRequestDto signUpRequest, String encodedPassword) {
        return UserEntity.builder()
                .email(signUpRequest.email().email())
                .phoneNum(signUpRequest.phoneNum().phoneNumber())
                .password(encodedPassword)
                .nickname(signUpRequest.nickname().nickname())
                .role(Role.valueOf(signUpRequest.role().role()))
                .univName(signUpRequest.univName().univName())
                .emailVerified(true)
                .phoneVerified(true)
                .profileImageUrl(null) // 프로필 이미지 기본값 설정 (NULL)
                .build();
    }

    public void saveRefreshToken(String phone, String refreshToken){
        redisTemplate.opsForValue().set(phone, refreshToken, 7, TimeUnit.DAYS);
    }
}
