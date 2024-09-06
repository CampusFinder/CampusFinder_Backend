package com.example.campusfinder.sms.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import java.time.Duration;
import com.example.campusfinder.sms.constant.Constant;


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

    private final StringRedisTemplate redisTemplate;

    //redis에서 사용할 키 생성
    //입력이 01031005136이면 반환시 sms:01031005136
    private String getRedisKey(String phoneNumber){
        return Constant.PREFIX + phoneNumber;
    }

    //redis에 인증번호 저장, 300초동안 유지
    public void createSmsCertification(String phoneNumber, String certificationNumber){
        redisTemplate.opsForValue()
                .set(getRedisKey(phoneNumber), certificationNumber, Duration.ofSeconds(Constant.LIMIT_TIME));
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

    //휴대폰 인증이 완료되었는지 확인하는 메서드
    public boolean isPhoneVerified(String phoneNumber){
        String redisKey=Constant.PREFIX + phoneNumber;
        String value=redisTemplate.opsForValue().get(redisKey);
        return "VERIFIED".equals(value);
    }

    //휴대폰 인증 성공 시 호출되는 메서드
    public void verifyPhone(String phoneNumber){
        String redisKey=Constant.PREFIX+phoneNumber;
        redisTemplate.opsForValue().set(redisKey,"VERIFIED");
    }

}
