package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.dto.deserializer.RoleDtoDeserializer;
import com.example.campusfinder.user.validates.RoleValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
@JsonDeserialize(using = RoleDtoDeserializer.class)
public record RoleDto(String role) {
}
