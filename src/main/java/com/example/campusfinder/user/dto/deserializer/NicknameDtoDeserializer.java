package com.example.campusfinder.user.dto.deserializer;

import com.example.campusfinder.user.dto.request.element.NicknameDto;
import com.example.campusfinder.user.validates.NicknameValidator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * packageName    : com.example.campusfinder.user.dto.deserializer
 * fileName       : NicknameDtoDeserializer
 * author         : tlswl
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        tlswl       최초 생성
 */
public class NicknameDtoDeserializer extends JsonDeserializer<NicknameDto> {
    private final NicknameValidator nicknameValidator = new NicknameValidator();

    @Override
    public NicknameDto deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        TextNode node = p.getCodec().readTree(p);
        String nickname = node.textValue();

        // Validate nickname using NicknameValidator
        nicknameValidator.validate(nickname);

        return new NicknameDto(nickname);
    }
}