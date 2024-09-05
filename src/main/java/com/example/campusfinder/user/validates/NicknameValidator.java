package com.example.campusfinder.user.validates;

import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.campusfinder.user.validates
 * fileName       : NicknameValidator
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Component
public class NicknameValidator implements Validator<String>{

    @Override
    public void validate(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 빈칸일 수 없습니다.");
        }
        if (nickname.length() < 2 || nickname.length() > 10) {
            throw new IllegalArgumentException("닉네임은 2자 이상, 10자 이하여야 합니다.");
        }
    }
}
