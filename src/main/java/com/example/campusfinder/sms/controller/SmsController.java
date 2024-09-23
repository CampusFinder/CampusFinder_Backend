package com.example.campusfinder.sms.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.sms.dto.SmsRequest;
import com.example.campusfinder.sms.service.SmsService;
import com.example.campusfinder.email.service.EmailVerificationService; // 이메일 인증 서비스 추가
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="문자 인증 API")
public class SmsController {

    private final SmsService smsService;
    private final EmailVerificationService emailVerificationService; // 이메일 인증 서비스 의존성 추가

    @Operation(summary = "SMS 인증번호 전송 API", description = "이메일 인증을 완료한 사용자의 핸드폰 번호로 SMS 인증 코드를 전송")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "SMS 전송 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "이메일 인증이 완료되지 않음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/send")
    public ResponseEntity<BaseResponse> sendSms(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "이메일과 전화번호를 입력하여 SMS 인증 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "phoneNum": "01012345678",
                                  "email": "tlswlgns1003@sju.ac.kr"
                                }
                                """),
                            schema = @Schema(implementation = SmsRequest.class)
                    )
            )SmsRequest request) {
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
    @Operation(summary = "SMS 인증번호 확인 API", description = "사용자가 입력한 SMS 인증 번호가 유효한지 확인, 5분지나면 무효화")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "SMS 인증 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 코드가 유효하지 않음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/verify")
    public ResponseEntity<BaseResponse> verifySms(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "SMS 인증 확인",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "phoneNum": "01012345678",
                                  "code": "123456"
                                }
                                """),
                            schema = @Schema(implementation = SmsRequest.class)
                    )
            )
            SmsRequest request) {
        smsService.verifySms(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SMS 인증 성공"));
    }
}
