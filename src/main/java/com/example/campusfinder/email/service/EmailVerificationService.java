package com.example.campusfinder.email.service;

import com.example.campusfinder.email.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import static com.univcert.api.UnivCert.*;

/**
 * packageName    : com.example.campusfinder.email.service
 * fileName       : EmailVerificationService
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final StringRedisTemplate redisTemplate;
    private final String univCertApiKey = "6a6d4b2b-1897-46f2-8de9-ae857a32db75";  // 실제 API 키로 교체하세요
    private final static long EXPIRATION_TIME = 300L; // 3분

    public void sendVerificationCode(EmailRequest emailRequest) throws IOException {
        String redisKey = "email:verification:" + emailRequest.email();

        // 기존 Redis 상태 확인 및 초기화
        if (redisTemplate.hasKey(redisKey)) {
            String status = redisTemplate.opsForValue().get(redisKey);
            System.out.println("Current Status: " + status);

            if ("PENDING".equals(status)) {
                // UnivCert API의 상태를 초기화
                clear(univCertApiKey, emailRequest.email());
                redisTemplate.delete(redisKey);
                System.out.println("Existing PENDING status cleared and deleted for: " + emailRequest.email());
            }
        }

        // UnivCert API를 사용해 인증 코드 발송
        certify(univCertApiKey, emailRequest.email(), emailRequest.universityName(), true);

        // Redis에 새로운 인증 상태 저장 (3분 동안 유효)
        redisTemplate.opsForValue().set(redisKey, "PENDING", Duration.ofSeconds(EXPIRATION_TIME));
        System.out.println("New PENDING status set for: " + emailRequest.email());
    }

    public boolean verifyCode(String email, String univName, int code) throws IOException {
        String redisKey = "email:verification:" + email;
        String status = redisTemplate.opsForValue().get(redisKey);

        // 인증 상태가 없거나 제한시간이 지난 경우
        if (status == null) {
            throw new IllegalArgumentException("인증 요청이 유효하지 않습니다. 제한 시간이 지났거나 요청이 만료되었습니다.");
        }

        // UnivCert API를 사용해 인증 코드 검증
        Map<String, Object> apiResponse = certifyCode(univCertApiKey, email, univName, code);

        // 인증 성공 여부 확인
        boolean isVerified = Boolean.TRUE.equals(apiResponse.get("success"));

        if (isVerified) {
            // 인증이 성공하면 상태를 COMPLETED로 변경
            redisTemplate.opsForValue().set(redisKey, "COMPLETED");
            redisTemplate.expire(redisKey, Duration.ofMinutes(10)); // 인증 완료 후 10분 동안 유지
        }

        return isVerified;
    }

    public boolean isVerificationPending(String email) {
        String redisKey = "email:verification:" + email;
        return redisTemplate.hasKey(redisKey) && "PENDING".equals(redisTemplate.opsForValue().get(redisKey));
    }
}
