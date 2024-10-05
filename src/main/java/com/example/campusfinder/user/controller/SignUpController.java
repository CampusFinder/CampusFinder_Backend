package com.example.campusfinder.user.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.user.dto.request.signup.NickNameCheckRequest;
import com.example.campusfinder.user.dto.request.signup.PasswordCheckRequest;
import com.example.campusfinder.user.dto.request.signup.SignUpRequestDto;
import com.example.campusfinder.user.service.UserService;
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
 * packageName    : com.example.campusfinder.user.controller
 * fileName       : SignUpController
 * author         : tlswl
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        tlswl       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
@Tag(name = "회원가입 API")
public class SignUpController {

    private final UserService userService;

    @Operation(summary = "닉네임 중복 확인 API", description = "사용자가 입력한 닉네임이 중복되는지 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 사용 가능",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 닉네임",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/nickname-check")
    public ResponseEntity<BaseResponse> checkNickname(@RequestBody NickNameCheckRequest nicknameRequest) {
        if (userService.isNicknameTaken(nicknameRequest.nickname())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(BaseResponse.ofError(HttpStatus.CONFLICT.value(), "중복된 닉네임입니다."));
        }
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "닉네임 사용이 가능합니다."));
    }

    @Operation(summary = "비밀번호 확인 API", description = "사용자가 입력한 비밀번호 유효성 검사")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 조건 충족",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호 조건 불충족",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/password-check")
    public ResponseEntity<BaseResponse> checkPassword(@RequestBody PasswordCheckRequest passwordCheckRequest) {
        if (!userService.isPasswordValid(passwordCheckRequest.password())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.ofError(HttpStatus.BAD_REQUEST.value(), "비밀번호 정규식 일치해야합니다(영문, 숫자, 특수문자 포함 8자리)"));
        }
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), "비밀번호가 양식 충족."));
    }

    @Operation(summary = "회원가입 API", description = "회원 가입 요청을 처리하고, 성공 시 완료 메시지를 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 이메일 또는 전화번호",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BaseResponse> signUp(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "이메일: 형식지켜서, 닉네임:2~10글자, 비밀번호:4자리 이상, 번호:11자리, role:STUDENT, PROFESSOR",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "회원가입 예시",
                                    value = """
                                            {
                                                "role": "STUDENT",
                                                "email": "tlswlgns1003@sju.ac.kr",
                                                "phoneNum": "01012345678",
                                                "password": "1234567",
                                                "nickname": "boong",
                                                "univName": "세종대학교"
                                            }
                                            """
                            ),
                            schema = @Schema(implementation = SignUpRequestDto.class)
                    )
            ) @RequestBody SignUpRequestDto signUpRequest){
        userService.signUpUser(signUpRequest);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(),"회원가입이 완료되었습니다."));
    }
    //
}