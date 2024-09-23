package com.example.campusfinder.user.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.user.dto.request.signin.SignInRequestDto;
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

import java.util.Map;

/**
 * packageName    : com.example.campusfinder.user.controller
 * fileName       : SignInController
 * author         : tlswl
 * date           : 2024-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-07        tlswl       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signin")
@Tag(name = "로그인 API")
public class SignInController {

    private final UserService userService;

    @Operation(summary = "로그인 API", description = "유저가 핸드폰 번호와 비밀번호로 로그인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(
                            implementation = BaseResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(
                            implementation = BaseResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 실패",
                    content = @Content(schema = @Schema(
                            implementation = BaseResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BaseResponse> singIn(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "로그인 요청 데이터",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "로그인 예시",
                            value = """
                                            {
                                                "phoneNum": "01012345678",
                                                "password": "password123"
                                            }
                                            """
                    ),
                    schema = @Schema(implementation = SignInRequestDto.class)
            )
    ) @RequestBody SignInRequestDto signInRequestDto){
        Map<String, String> tokens = userService.signInUser(signInRequestDto);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(),tokens));
    }
}
