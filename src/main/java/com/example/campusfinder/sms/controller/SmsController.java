package com.example.campusfinder.sms.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.sms.dto.SmsRequest;
import com.example.campusfinder.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/sms-certifications")
public class SmsController {

    private final SmsService smsService;

    @PostMapping
    public ResponseEntity<BaseResponse> sendSms(@RequestBody SmsRequest request) {
        smsService.sendSms(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SMS 전송 성공"));
    }

    // 인증번호 확인
    @PostMapping("/verify")
    public ResponseEntity<BaseResponse> verifySms(@RequestBody SmsRequest request) {
        smsService.verifySms(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "인증 성공"));
    }
}
