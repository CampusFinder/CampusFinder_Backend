package com.example.campusfinder.user.dto.deserializer;

import com.example.campusfinder.user.dto.request.element.RoleDto;
import com.example.campusfinder.user.validates.RoleValidator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

/**
 * packageName    : com.example.campusfinder.user.dto.deserializer
 * fileName       : RoleDtoDeserializer
 * author         : tlswl
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        tlswl       최초 생성
 */
public class RoleDtoDeserializer extends JsonDeserializer<RoleDto> {
    private final RoleValidator roleValidator = new RoleValidator();

    @Override
    public RoleDto deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        TextNode node = p.getCodec().readTree(p);
        String role = node.textValue();

        // Validate role using RoleValidator
        roleValidator.validate(role);

        return new RoleDto(role);
    }
}