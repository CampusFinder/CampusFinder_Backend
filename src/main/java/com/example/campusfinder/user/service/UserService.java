package com.example.campusfinder.user.service;

import com.example.campusfinder.user.dto.request.signup.SignUpRequestDto;
import com.example.campusfinder.user.entity.UserEntity;
import com.example.campusfinder.user.repository.UserRepository;
import com.example.campusfinder.user.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserUtils userUtils;

    @Transactional
    public void signUpUser(SignUpRequestDto signUpRequest) {
        //이미 가입된 회원이면 이메일인증과 핸드폰 인증이 되지 않도록 방지
        if(userRepository.existsByEmail(signUpRequest.email().email())){
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        if(userRepository.existsByPhone(signUpRequest.phone().phoneNumber())){
            throw new IllegalArgumentException("이미 가입된 전화번호 입니다.");
        }


        // 이메일 인증 여부와 전화번호 인증 여부를 개별적으로 확인
        boolean isEmailVerified = userUtils.isEmailVerified(signUpRequest.email().email());
        boolean isPhoneVerified = userUtils.isPhoneVerified(signUpRequest.phone().phoneNumber());

        // 인증 여부를 로그로 출력
        System.out.println("이메일 인증 여부: " + isEmailVerified);
        System.out.println("전화번호 인증 여부: " + isPhoneVerified);

        if (!isEmailVerified || !isPhoneVerified) {
            throw new IllegalArgumentException("이메일과 sms 인증이 완료되지 않았습니다");
        }

        UserEntity newUser = userUtils.createUserEntity(signUpRequest);
        userRepository.save(newUser);
    }
}
