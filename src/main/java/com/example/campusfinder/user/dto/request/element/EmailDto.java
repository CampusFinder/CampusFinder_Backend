package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.dto.deserializer.EmailDtoDeserializer;
import com.example.campusfinder.user.validates.EmailValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
@JsonDeserialize(using = EmailDtoDeserializer.class)
public record EmailDto(String email) {
}
