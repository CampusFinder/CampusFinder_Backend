package com.example.campusfinder.token.dto;

/**
 * packageName    : com.example.campusfinder.token.dto
 * fileName       : TokenResponse
 * author         : tlswl
 * date           : 2024-10-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-05        tlswl       최초 생성
 */
public record TokenResponse(String accessToken, String refreshToken) {
}
