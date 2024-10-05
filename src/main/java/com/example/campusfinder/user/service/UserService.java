package com.example.campusfinder.user.service;

import com.example.campusfinder.core.security.JwtTokenProvider;
import com.example.campusfinder.user.dto.request.signin.SignInRequestDto;
import com.example.campusfinder.user.dto.request.signup.SignUpRequestDto;
import com.example.campusfinder.user.dto.response.SignInResponseDto;
import com.example.campusfinder.user.entity.UserEntity;
import com.example.campusfinder.user.repository.UserRepository;
import com.example.campusfinder.user.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUpUser(SignUpRequestDto signUpRequest) {
        //이미 가입된 회원이면 이메일인증과 핸드폰 인증이 되지 않도록 방지
        if(userRepository.existsByEmail(signUpRequest.email().email())){
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        if(userRepository.existsByPhoneNum(signUpRequest.phoneNum().phoneNumber())){
            throw new IllegalArgumentException("이미 가입된 전화번호 입니다.");
        }

        // 이메일 인증 여부와 전화번호 인증 여부를 개별적으로 확인
        boolean isEmailVerified = userUtils.isEmailVerified(signUpRequest.email().email());
        boolean isPhoneVerified = userUtils.isPhoneVerified(signUpRequest.phoneNum().phoneNumber());

        // 인증 여부를 로그로 출력
        System.out.println("이메일 인증 여부: " + isEmailVerified);
        System.out.println("전화번호 인증 여부: " + isPhoneVerified);

        if (!isEmailVerified || !isPhoneVerified) {
            throw new IllegalArgumentException("이메일과 sms 인증이 완료되지 않았습니다");
        }

        //비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(signUpRequest.password().password());

        UserEntity newUser = userUtils.createUserEntity(signUpRequest, encodedPassword);
        userRepository.save(newUser);
    }

    //로그인
    public SignInResponseDto signInUser(SignInRequestDto signInRequest){
        UserEntity user = userRepository.findByPhoneNum(signInRequest.phoneNum())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUserIdx());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserIdx());
        // Refresh Token 저장 (Redis)
        userUtils.saveRefreshToken(user.getUserIdx().toString(), refreshToken);

        return new SignInResponseDto(accessToken, refreshToken);
    }

    // 닉네임 중복 체크 메서드
    public boolean isNicknameTaken(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean isPasswordValid(String password) {
        // 영문, 숫자, 특수문자가 각각 하나 이상 포함된 8자리 이상의 비밀번호 유효성 검사 정규식
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return Pattern.matches(passwordPattern, password);
    }
}
