package com.example.campusfinder.email.utils;

import com.example.campusfinder.email.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import static com.univcert.api.UnivCert.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class EmailVerificationUtils {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;  // JSON 파싱을 위한 ObjectMapper
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
        // 교수 인증 로직 구현
        // 예시로 교수 인증 로직 추가
        String redisKey = "email:verification:" + email;
        return redisTemplate.opsForValue().get(redisKey).equals(String.valueOf(code));
    }

    public void sendProfessorVerificationCode(String email) {
        // 교수 인증 코드를 생성 및 Redis에 저장하는 로직 추가
        String code = "123456";  // 예시로 고정된 코드 사용, 실제로는 랜덤 생성 필요
        String redisKey = "email:verification:" + email;
        redisTemplate.opsForValue().set(redisKey, code, Duration.ofMinutes(10));
        // 이메일로 발송하는 로직 추가 필요
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
