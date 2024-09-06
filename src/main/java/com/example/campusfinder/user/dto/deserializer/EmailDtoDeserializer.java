package com.example.campusfinder.user.dto.deserializer;

import com.example.campusfinder.user.dto.request.element.EmailDto;
import com.example.campusfinder.user.validates.EmailValidator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * packageName    : com.example.campusfinder.user.dto.deserializer
 * fileName       : EmailDtoDeserializer
 * author         : tlswl
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        tlswl       최초 생성
 */
public class EmailDtoDeserializer extends JsonDeserializer<EmailDto> {
    private final EmailValidator emailValidator = new EmailValidator();

    @Override
    public EmailDto deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        TextNode node = p.getCodec().readTree(p);
        String email = node.textValue();

        // Validate email using EmailValidator
        emailValidator.validate(email);

        return new EmailDto(email);
    }
}