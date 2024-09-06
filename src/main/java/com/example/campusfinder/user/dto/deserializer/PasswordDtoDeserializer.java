package com.example.campusfinder.user.dto.deserializer;

import com.example.campusfinder.user.dto.request.element.PasswordDto;
import com.example.campusfinder.user.validates.PasswordValidator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * packageName    : com.example.campusfinder.user.dto.deserializer
 * fileName       : PasswordDtoDeserializer
 * author         : tlswl
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        tlswl       최초 생성
 */
public class PasswordDtoDeserializer extends JsonDeserializer<PasswordDto> {
    private final PasswordValidator passwordValidator = new PasswordValidator();

    @Override
    public PasswordDto deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        TextNode node = p.getCodec().readTree(p);
        String password = node.textValue();

        // Validate password using PasswordValidator
        passwordValidator.validate(password);

        return new PasswordDto(password);
    }
}