package com.example.campusfinder.email.utils;

import com.example.campusfinder.email.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Random;

import static com.univcert.api.UnivCert.*;


@Component
@RequiredArgsConstructor
public class EmailVerificationUtils {

    private final StringRedisTemplate redisTemplate;
    private final EmailSender emailSender;
    @Value("${univcert.api-key}")
    private String univCertApiKey;
    private final static long EXPIRATION_TIME = 600L; // 5분

    public void sendVerificationCode(EmailRequest emailRequest) throws IOException {
        String redisKey = "email:verification:" + emailRequest.email();
        certify(univCertApiKey, emailRequest.email(), emailRequest.univName(), true);
        redisTemplate.opsForValue().set(redisKey, "PENDING", Duration.ofSeconds(EXPIRATION_TIME));
    }


    public boolean verifyStudentCode(String email, String univName, int code) throws IOException {
        String redisKey = "email:verification:" + email;
        Map<String, Object> response = certifyCode(univCertApiKey, email, univName, code);
        boolean isVerified = (boolean) response.get("success");

        if (isVerified) {
            redisTemplate.opsForValue().set(redisKey, "COMPLETED");
            redisTemplate.expire(redisKey, Duration.ofMinutes(10));
        }

        return isVerified;
    }

    public boolean verifyProfessorCode(String email, int code) {
        String redisKey = "email:verification:" + email;
        return redisTemplate.opsForValue().get(redisKey).equals(String.valueOf(code));
    }

    // 랜덤 6자리 숫자 생성 함수
    private String generateVerificationCode() {
        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000);  // 100000 ~ 999999 사이의 랜덤 숫자
        return String.valueOf(randomCode);
    }

    // 교수 인증 로직 수정: 이메일 발송을 위한 로직 추가
    public void sendProfessorVerificationCode(String email) {
        // 랜덤 6자리 인증번호 생성
        String code = generateVerificationCode();
        String redisKey = "email:verification:" + email;

        // Redis에 인증번호 저장 (유효시간 10분)
        redisTemplate.opsForValue().set(redisKey, code, Duration.ofMinutes(10));

        // 인증번호 이메일 전송
        String subject = "CampusFinder 교수 인증번호";
        String text = "안녕하세요, CampusFinder입니다.\n\n" + "교수님의 인증번호는 " + code + " 입니다.";
        emailSender.sendEmail(email, subject, text);  // 이메일 발송
    }

    public void clearPendingVerification(String email) throws IOException {
        String redisKey = "email:verification:" + email;
        if (redisTemplate.hasKey(redisKey)) {
            String status = redisTemplate.opsForValue().get(redisKey);
            if ("PENDING".equals(status)) {
                clear(univCertApiKey, email);
                redisTemplate.delete(redisKey);
            }
        }
    }

    public boolean isVerificationPending(String email) {
        String redisKey = "email:verification:" + email;
        return redisTemplate.hasKey(redisKey) && "PENDING".equals(redisTemplate.opsForValue().get(redisKey));
    }
}
