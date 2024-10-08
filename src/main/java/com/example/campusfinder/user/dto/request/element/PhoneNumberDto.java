package com.example.campusfinder.user.dto.request.element;

import com.example.campusfinder.user.dto.deserializer.PhoneNumberDtoDeserializer;
import com.example.campusfinder.user.validates.PhoneNumberValidator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
@JsonDeserialize(using = PhoneNumberDtoDeserializer.class)
public record PhoneNumberDto(String phoneNumber) {
    @JsonValue
    public String getPhoneNum(){
        return phoneNumber;
    }
}
