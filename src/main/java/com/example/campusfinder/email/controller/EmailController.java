package com.example.campusfinder.email.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.email.dto.EmailRequest;
import com.example.campusfinder.email.dto.EmailVerifyRequest;
import com.example.campusfinder.email.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certification/email")
@Tag(name = "이메일 인증 API")
public class EmailController {

    private final EmailVerificationService emailVerificationService;

    @Operation(summary = "이메일 인증번호 전송 API", description = "입력한 이메일로 인증코드를 전송")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증코드 전송 성공",
                    content = @Content(schema = @Schema(
                            implementation = BaseResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(
                            implementation = BaseResponse.class))),
    })
    @PostMapping("/send")
    public ResponseEntity<BaseResponse> sendVerificationCode(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "role: PROFESSOR, STUDENT 중 1개 선택(enum), univName: 00대학교 형식, email: 교수임시이메일(tlswlgns1003@naver.com)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "role": "STUDENT",
                                      "email": "tlswlgns1003@sju.ac.kr",
                                      "univName": "세종대학교"
                                    }
                                    """),
                            schema = @Schema(implementation = EmailRequest.class)
                    ))
           @org.springframework.web.bind.annotation.RequestBody EmailRequest emailRequest) throws IOException {
        try {
            emailVerificationService.sendVerificationCode(emailRequest);
            return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "인증 코드가 전송되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @Operation(summary = "인증 코드 확인", description = "사용자가 입력한 인증 코드가 유효한지 확인, 메일받고 5분지나면 다시 인증 받아야함")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 코드",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/verify")
    public ResponseEntity<BaseResponse> verifyCode(
            @RequestBody(description = "이메일 인증 코드 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "email": "tlswlgns1003@sju.ac.kr",
                                      "univName": "세종대학교",
                                      "code": 123456,
                                      "role": "STUDENT"
                                    }
                                    """),
                            schema = @Schema(implementation = EmailVerifyRequest.class)
                    ))
            @org.springframework.web.bind.annotation.RequestBody EmailVerifyRequest request) throws IOException {
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

    @Operation(summary = "이메일 인증 상태 확인", description = "이메일 인증이 완료되었는지 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 완료",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "이메일 인증 미완료",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/status")
    public ResponseEntity<BaseResponse> checkVerificationStatus(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "확인할 이메일 주소",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "email": "tlswlgns1003@sju.ac.kr"
                                    }
                                    """),
                            schema = @Schema(implementation = String.class)
                    )) @RequestParam String email) {
        boolean isPending = emailVerificationService.isVerificationPending(email);

        if (isPending) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.ofError(HttpStatus.UNAUTHORIZED.value(), "이메일 인증이 아직 완료되지 않았습니다."));
        } else {
            return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "이메일 인증이 완료되었습니다."));
        }
    }
}
