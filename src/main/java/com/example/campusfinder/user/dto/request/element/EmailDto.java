package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.validates.EmailValidator;

/**
 * packageName    : com.example.campusfinder.user.dto.request
 * fileName       : EmailDto
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public record EmailDto(String email) {
    private static final EmailValidator emailValidator = new EmailValidator();

    public EmailDto{
        emailValidator.validate(email);
    }
}
