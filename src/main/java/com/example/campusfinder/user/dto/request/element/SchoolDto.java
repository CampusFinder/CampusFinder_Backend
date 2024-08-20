package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.validates.SchoolValidator;

/**
 * packageName    : com.example.campusfinder.user.dto.request
 * fileName       : SchoolDto
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public record SchoolDto(String school) {
    private static final SchoolValidator schoolValidator = new SchoolValidator();

    public SchoolDto{
        schoolValidator.validate(school);
    }
}
