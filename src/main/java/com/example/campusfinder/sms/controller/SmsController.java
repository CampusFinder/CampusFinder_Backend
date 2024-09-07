package com.example.campusfinder.sms.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.sms.dto.SmsRequest;
import com.example.campusfinder.sms.service.SmsService;
import com.example.campusfinder.email.service.EmailVerificationService; // 이메일 인증 서비스 추가
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * packageName    : com.example.campusfinder.sms.controller
 * fileName       : SmsController
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certification/sms")
public class SmsController {

    private final SmsService smsService;
    private final EmailVerificationService emailVerificationService; // 이메일 인증 서비스 의존성 추가

    @PostMapping("/send")
    public ResponseEntity<BaseResponse> sendSms(@RequestBody SmsRequest request) {
        boolean isVerified = emailVerificationService.isEmailVerified(request.email());
        System.out.println("이메일 인증 상태: " + isVerified);  // 로그 추가

        if (!isVerified) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.ofError(HttpStatus.FORBIDDEN.value(), "이메일 인증이 완료되지 않았습니다."));
        }

        smsService.sendSms(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SMS 전송 성공"));
    }

    // 인증번호 확인
    @PostMapping("/verify")
    public ResponseEntity<BaseResponse> verifySms(@RequestBody SmsRequest request) {
        smsService.verifySms(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SMS 인증 성공"));
    }
}
