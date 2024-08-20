package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.validates.PhoneNumberValidator;

/**
 * packageName    : com.example.campusfinder.user.dto.request
 * fileName       : PhoneNumberDto
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public record PhoneNumberDto(String phoneNumber) {
    private static final PhoneNumberValidator phoneNumberValidator=new PhoneNumberValidator();

    public PhoneNumberDto{
        phoneNumberValidator.validate(phoneNumber);
    }
}
