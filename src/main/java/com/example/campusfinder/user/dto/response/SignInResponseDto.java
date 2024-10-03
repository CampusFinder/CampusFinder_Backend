package com.example.campusfinder.user.dto.response;

/**
 * packageName    : com.example.campusfinder.user.dto.response
 * fileName       : SignInResponseDto
 * author         : tlswl
 * date           : 2024-10-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-01        tlswl       최초 생성
 */
public record SignInResponseDto(
        String accessToken,
        String refreshToken
) {
}
