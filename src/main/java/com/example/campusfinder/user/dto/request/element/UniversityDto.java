package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.dto.deserializer.UniversityDtoDeserializer;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
@JsonDeserialize(using = UniversityDtoDeserializer.class)
public record UniversityDto(String univName) {
    @JsonValue
    public String getUnivName(){
        return univName;
    }
}
