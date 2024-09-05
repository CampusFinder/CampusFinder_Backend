package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.validates.PasswordValidator;

/**
 * packageName    : com.example.campusfinder.user.dto.request
 * fileName       : PasswordDto
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public record PasswordDto(String password) {
    private static final PasswordValidator passwordValidator = new PasswordValidator();

    public PasswordDto{
        passwordValidator.validate(password);
    }
}
