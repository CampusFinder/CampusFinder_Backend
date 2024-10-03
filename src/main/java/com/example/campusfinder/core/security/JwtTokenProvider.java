package com.example.campusfinder.core.security;

import com.example.campusfinder.user.service.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * packageName    : com.example.campusfinder.core.security
 * fileName       : TokenProvider
 * author         : tlswl
 * date           : 2024-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-07        tlswl       최초 생성
 */
@Component
public class JwtTokenProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private final long accessTokenDuration = 1000L * 60 * 60 * 24 * 7;
    private final long refreshTokenDuration = 1000L * 60 * 60 * 24 * 7;

    public String generateAccessToken(Long userIdx) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenDuration);

        return Jwts.builder()
                .setSubject(userIdx.toString()) // userIdx를 Subject로 설정
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String generateRefreshToken(Long userIdx) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenDuration);

        return Jwts.builder()
                .setSubject(userIdx.toString()) // userIdx를 Subject로 설정
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUserPhoneFromToken(String token){
       return Jwts.parser()
               .setSigningKey(secretKey)
               .parseClaimsJws(token)
               .getBody()
               .getSubject();
    }

    public boolean validateToken(String token){
       try{
           Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
           return true;
       }
       catch(Exception e){
           return false;
       }
    }

    //헤더에서 jwt 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 값 반환
        }
        return null;
    }

    // JWT에서 사용자 정보 인증
    public Authentication getAuthentication(String phoneNum) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNum); // phone을 사용하여 UserDetails 로드
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //토큰에서 UserIdx 값 추출
    public Long getUserIdxFromToken(String token) {
        return Long.parseLong(Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    // 토큰에서 nickname 추출
    public String getNicknameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("nickname", String.class);
    }

}
