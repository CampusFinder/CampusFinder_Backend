package com.example.campusfinder.email.utils;

import com.example.campusfinder.email.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

import static com.univcert.api.UnivCert.*;

/**
 * packageName    : com.example.campusfinder.email.utils
 * fileName       : EmailVerificationUtils
 * author         : tlswl
 * date           : 2024-09-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-04        tlswl       최초 생성
 */
@Component
@RequiredArgsConstructor
public class EmailVerificationUtils {

    private final StringRedisTemplate redisTemplate;
    @Value("${univcert.api-key}")
    private String univCertApiKey;
    private final static long EXPIRATION_TIME = 300L; // 5분

    public void sendVerificationCode(EmailRequest emailRequest) throws IOException {
        String redisKey = "email:verification:" + emailRequest.email();
        certify(univCertApiKey, emailRequest.email(), emailRequest.univName(), true);
        redisTemplate.opsForValue().set(redisKey, "PENDING", Duration.ofSeconds(EXPIRATION_TIME));
    }

    public boolean verifyCode(String email, String univName, int code) throws IOException {
        String redisKey = "email:verification:" + email;
        boolean isVerified = certifyCode(univCertApiKey, email, univName, code).isEmpty();

        if (isVerified) {
            redisTemplate.opsForValue().set(redisKey, "COMPLETED");
            redisTemplate.expire(redisKey, Duration.ofMinutes(10)); // 인증 완료 후 10분 동안 유지
        }

        return isVerified;
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

    public boolean isVerificationExpired(String email) {
        String redisKey = "email:verification:" + email;
        return !redisTemplate.hasKey(redisKey);
    }

    public boolean isVerificationPending(String email) {
        String redisKey = "email:verification:" + email;
        return redisTemplate.hasKey(redisKey) && "PENDING".equals(redisTemplate.opsForValue().get(redisKey));
    }
}