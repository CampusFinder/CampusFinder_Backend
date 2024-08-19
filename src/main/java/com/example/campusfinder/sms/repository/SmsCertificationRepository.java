package com.example.campusfinder.sms.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

/**
 * packageName    : com.example.campusfinder.sms.repository
 * fileName       : SmsCertificationRepository
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@RequiredArgsConstructor
@Repository
public class SmsCertificationRepository {

    private static final String PREFIX="sms:";
    private static final int LIMIT_TIME= 300;
    private final StringRedisTemplate redisTemplate;

    //redis에서 사용할 키 생성
    //입력이 01031005136이면 반환히 sms:01031005136
    private String getRedisKey(String phoneNumber){
        return PREFIX + phoneNumber;
    }

    //redis에 인증번호 저장, 300초동안 유지
    public void createSmsCertification(String phoneNumber, String certificationNumber){
        redisTemplate.opsForValue()
                .set(getRedisKey(phoneNumber), certificationNumber, Duration.ofSeconds(LIMIT_TIME));
    }

    //redis에서 전화번호에 해당하는 인증번호 조회
    public String getSmsCertification(String phoneNumber){
        return redisTemplate.opsForValue().get(getRedisKey(phoneNumber));
    }

    //redis에서 전화번호에 해당하는 인증번호 삭제
    public void removeSmsCertification(String phoneNumber){
        redisTemplate.delete(getRedisKey(phoneNumber));
    }

    //redis에서 전화번호에 해당하는 인증번호 있는지 확인
    public boolean hasKey(String phoneNumber){
        return Boolean.TRUE.equals(redisTemplate.hasKey(getRedisKey(phoneNumber)));
    }
}
