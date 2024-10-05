package com.example.campusfinder.token.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.token.dto.RefreshTokenRequest;
import com.example.campusfinder.token.dto.TokenResponse;
import com.example.campusfinder.token.service.TokenService;
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
 * packageName    : com.example.campusfinder.token.controller
 * fileName       : TokenController
 * author         : tlswl
 * date           : 2024-10-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-05        tlswl       최초 생성
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "토큰 발급 API", description = "토큰 재발급을 위한 API")
public class TokenController {

    private final TokenService tokenService;

    @Operation(
            summary = "accessToken, refreshToken 모두 재발행",
            description = "유효한 RefreshToken을 사용하여 만료된 AccessToken을 재발급합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "AccessToken을 재발급하기 위한 RefreshToken 요청",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(
                                    name = "RefreshToken 요청 예시",
                                    value = """
                                    {
                                      "refreshToken": "eyJhbGc"
                                    }
                                    """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "AccessToken 재발급 성공",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "AccessToken 재발급 성공 예시",
                                    value = """
                                    {
                                      "status": 200,
                                      "description": "AccessToken 재발급 성공",
                                      "data": {
                                        "accessToken": "eyJhbGc",
                                        "refreshToken": "eyJhbGc"
                                      }
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 RefreshToken",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "유효하지 않은 RefreshToken 예시",
                                    value = """
                                    {
                                      "status": 401,
                                      "description": "유효하지 않은 RefreshToken입니다.",
                                      "data": null
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 - 필수 파라미터 누락 또는 값이 잘못된 경우",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "잘못된 요청 예시",
                                    value = """
                                    {
                                      "status": 400,
                                      "description": "잘못된 요청입니다. 필수 파라미터가 누락되었거나 값이 잘못되었습니다.",
                                      "data": null
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<TokenResponse>> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenResponse tokenResponse = tokenService.refreshAccessToken(refreshTokenRequest);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(), tokenResponse));
    }
}
