package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.validates.NicknameValidator;

/**
 * packageName    : com.example.campusfinder.user.dto.request
 * fileName       : NicknameDto
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public record NicknameDto(String nickname) {
    private static final NicknameValidator nicknameValidator = new NicknameValidator();

    public NicknameDto{
        nicknameValidator.validate(nickname);
    }
}
