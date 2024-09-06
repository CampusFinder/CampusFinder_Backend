package com.example.campusfinder.email.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.email.dto.EmailRequest;
import com.example.campusfinder.email.dto.EmailVerifyRequest;
import com.example.campusfinder.email.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email-certification")
public class EmailController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send")
    public ResponseEntity<BaseResponse> sendVerificationCode(@RequestBody EmailRequest emailRequest) throws IOException {
        try {
            emailVerificationService.sendVerificationCode(emailRequest);
            return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "인증 코드가 전송되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<BaseResponse> verifyCode(@RequestBody EmailVerifyRequest request) throws IOException {
        try {
            boolean isVerified = emailVerificationService.verifyCode(request.email(), request.univName(), request.code(), request.role());
            if (isVerified) {
                return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "이메일 인증에 성공했습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponse.ofError(HttpStatus.UNAUTHORIZED.value(), "인증 코드가 유효하지 않습니다."));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.ofError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<BaseResponse> checkVerificationStatus(@RequestParam String email) {
        boolean isPending = emailVerificationService.isVerificationPending(email);

        if (isPending) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.ofError(HttpStatus.UNAUTHORIZED.value(), "이메일 인증이 아직 완료되지 않았습니다."));
        } else {
            return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "이메일 인증이 완료되었습니다."));
        }
    }
}
