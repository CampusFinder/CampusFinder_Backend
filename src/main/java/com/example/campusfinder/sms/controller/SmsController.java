package com.example.campusfinder.sms.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.sms.dto.SmsRequest;
import com.example.campusfinder.sms.service.SmsService;
import com.example.campusfinder.sms.service.SmsValidationService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "회원가입 API")
public class SmsController {

    private final SmsService smsVerificationService;
    private final SmsValidationService smsValidationService;

    @Operation(
            summary = "SMS 인증번호 전송 API",
            description = "이메일 인증을 완료한 사용자의 핸드폰 번호로 SMS 인증 코드를 전송",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "SMS 전송을 위한 요청 본문. `phoneNum` 필드에는 사용자의 핸드폰 번호를, `email` 필드에는 이메일 주소를 입력",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SmsRequest.class),
                            examples = @ExampleObject(
                                    name = "SMS 인증번호 전송 예시",
                                    value = """
                    {
                      "phoneNum": "01031005136",
                      "email": "tlswlgns1003@sju.ac.kr"
                    }
                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "SMS 전송 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "description": "SMS 전송 성공",
                  "data": null
                }
            """))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "이메일 인증이 완료되지 않음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject(value = """
                {
                  "status": 403,
                  "description": "이메일 인증이 완료되지 않았습니다.",
                  "data": null
                }
            """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 - 필수 파라미터가 누락되었거나 값이 잘못된 경우",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "description": "잘못된 요청입니다.",
                  "data": null
                }
            """))
            )
    })
    @PostMapping("/send")
    public ResponseEntity<BaseResponse<Void>> sendSms(@RequestBody SmsRequest request) {
        try {
            smsVerificationService.sendSmsVerification(request);
            return ResponseEntity.ok(BaseResponse.ofSuccessWithoutData(HttpStatus.OK.value(), "SMS 전송 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.ofError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @Operation(
            summary = "SMS 인증번호 확인 API",
            description = "사용자가 입력한 SMS 인증 번호가 유효한지 확인하고, 인증이 완료되었는지 검증",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "SMS 인증 확인을 위한 요청 본문. `phoneNum` 필드에는 사용자의 핸드폰 번호를, `code` 필드에는 사용자가 입력한 인증 코드를 입력",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SmsRequest.class),
                            examples = @ExampleObject(
                                    name = "SMS 인증번호 확인 예시",
                                    value = """
                    {
                      "phoneNum": "01031005136",
                      "code": "123456"
                    }
                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "SMS 인증 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "description": "SMS 인증 성공",
                  "data": null
                }
            """))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 코드가 유효하지 않음 - 인증 코드가 틀리거나 만료된 경우",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject(value = """
                {
                  "status": 401,
                  "description": "인증 코드가 유효하지 않습니다.",
                  "data": null
                }
            """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 - 필수 파라미터가 누락되었거나 값이 잘못된 경우",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class), examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "description": "잘못된 요청입니다.",
                  "data": null
                }
            """))
            )
    })
    @PostMapping("/verify")
    public ResponseEntity<BaseResponse<Void>> verifySms(@RequestBody SmsRequest request) {
        try {
            smsValidationService.verifySms(request);
            return ResponseEntity.ok(BaseResponse.ofSuccessWithoutData(HttpStatus.OK.value(), "SMS 인증 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponse.ofError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }
}
