package com.example.campusfinder.token.service;

import com.example.campusfinder.core.security.JwtTokenProvider;
import com.example.campusfinder.token.dto.RefreshTokenRequest;
import com.example.campusfinder.token.dto.TokenResponse;
import com.example.campusfinder.user.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserUtils userUtils;

    public TokenResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();

        // RefreshToken의 유효성 확인
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 RefreshToken입니다.");
        }

        // RefreshToken에서 사용자 ID 추출
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(refreshToken);

        // Redis에서 저장된 RefreshToken과 비교 (userIdx를 key로 사용)
        String savedRefreshToken = userUtils.getRefreshTokenFromRedis(userIdx.toString());
        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 RefreshToken 입니다.");
        }

        // 새로운 AccessToken 및 RefreshToken 발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(userIdx);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userIdx);

        // Redis에 새로운 RefreshToken 저장 (userIdx를 key로 사용)
        userUtils.saveRefreshToken(userIdx.toString(), newRefreshToken);

        // 새로운 AccessToken과 RefreshToken 반환
        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
