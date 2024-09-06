package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.dto.deserializer.NicknameDtoDeserializer;
import com.example.campusfinder.user.validates.NicknameValidator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
@JsonDeserialize(using = NicknameDtoDeserializer.class)
public record NicknameDto(String nickname) {
}