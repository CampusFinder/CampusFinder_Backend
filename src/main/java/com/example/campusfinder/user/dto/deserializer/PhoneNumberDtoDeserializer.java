package com.example.campusfinder.user.dto.deserializer;

import com.example.campusfinder.user.dto.request.element.PhoneNumberDto;
import com.example.campusfinder.user.validates.PhoneNumberValidator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * packageName    : com.example.campusfinder.user.dto.deserializer
 * fileName       : PhoneNumberDeserializer
 * author         : tlswl
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        tlswl       최초 생성
 */
public class PhoneNumberDtoDeserializer extends JsonDeserializer<PhoneNumberDto> {
    private final PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();

    @Override
    public PhoneNumberDto deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        TextNode node = p.getCodec().readTree(p);
        String phoneNumber = node.textValue();
        phoneNumberValidator.validate(phoneNumber);

        return new PhoneNumberDto(phoneNumber);
    }
}
