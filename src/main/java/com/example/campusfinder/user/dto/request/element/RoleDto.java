package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.validates.RoleValidator;

/**
 * packageName    : com.example.campusfinder.user.dto.request
 * fileName       : RoleDto
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public record RoleDto(String role) {
    private static final RoleValidator roleValidator=new RoleValidator();

    public RoleDto{
        roleValidator.validate(role);
    }
}
