package com.example.campusfinder.email.service;

import com.example.campusfinder.email.dto.EmailRequest;
import com.example.campusfinder.email.repository.EmailVerificationRepository;
import com.example.campusfinder.email.utils.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProfessorEmailService implements EmailService {
    private final StringRedisTemplate redisTemplate;
    private final EmailSender emailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    @Value("${univcert.api.key}")
    private String univCertApiKey;

    @Override
    public void sendVerificationCode(EmailRequest emailRequest) {
        // 이메일 형식이 올바른지 확인
        if (!isValidEmailFormat(emailRequest.email())) {
            throw new IllegalArgumentException("이메일 형식을 맞춰주세요.");
        }

        // 이미 회원가입된 이메일인지 확인
        if (emailVerificationRepository.isRegistered(emailRequest.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 새로 이메일 인증 진행
        String redisKey = "email:verification:" + emailRequest.email();
        clearPendingVerification(emailRequest.email());
        String code = generateVerificationCode();
        redisTemplate.opsForValue().set(redisKey, code, Duration.ofMinutes(10));

        // 이메일 전송 로직 추가
        String subject = "CampusFinder 교수 인증번호";
        String text = "안녕하세요, CampusFinder입니다.\n\n" + "교수님의 인증번호는 " + code + " 입니다.";
        emailSender.sendEmail(emailRequest.email(), subject, text);  // 이메일 발송
    }

    @Override
    public boolean verifyCode(String email, String univName, int code) {
        String redisKey = "email:verification:" + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        if (storedCode == null) {
            clearPendingVerification(email); // Redis 초기화
            throw new IllegalArgumentException("유효시간이 지났습니다. 다시 인증을 받아주세요.");
        }

        return storedCode.equals(String.valueOf(code));
    }

    @Override
    public void clearPendingVerification(String email) {
        String redisKey = "email:verification:" + email;
        redisTemplate.delete(redisKey);
    }

    @Override
    public boolean isVerificationPending(String email) {
        String redisKey = "email:verification:" + email;
        return redisTemplate.hasKey(redisKey);
    }

    @Override
    public boolean isValidEmailFormat(String email) {
        // 이메일 형식 검증 정규 표현식
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000);  // 100000 ~ 999999 사이의 랜덤 숫자
        return String.valueOf(randomCode);
    }
}
