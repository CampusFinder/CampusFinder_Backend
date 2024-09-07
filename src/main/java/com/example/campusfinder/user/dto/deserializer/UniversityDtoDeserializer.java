package com.example.campusfinder.user.dto.deserializer;

import com.example.campusfinder.user.dto.request.element.UniversityDto;
import com.example.campusfinder.user.validates.UniversityValidator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * packageName    : com.example.campusfinder.user.dto.deserializer
 * fileName       : SchoolDtoDeserializer
 * author         : tlswl
 * date           : 2024-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-07        tlswl       최초 생성
 */
public class UniversityDtoDeserializer extends JsonDeserializer<UniversityDto> {
    private final UniversityValidator schoolValidator = new UniversityValidator();

    @Override
    public UniversityDto deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        TextNode node = p.getCodec().readTree(p);
        String school = node.textValue();

        schoolValidator.validate(school);

        return new UniversityDto(school);
    }
}
