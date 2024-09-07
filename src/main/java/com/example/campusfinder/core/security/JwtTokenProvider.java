package com.example.campusfinder.core.security;

import com.example.campusfinder.user.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private final long accessTokenDuration = 1000L * 60 * 30; //30분
    private final long refreshTokenDuration = 1000L * 60 * 60 * 24 * 7;

    public String generateAccessToken(String phone) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenDuration);

        return Jwts.builder()
                .setSubject(phone)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String generateRefreshToken(String phone) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenDuration);

        return Jwts.builder()
                .setSubject(phone)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUserEmailFromToken(String token){
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

}
