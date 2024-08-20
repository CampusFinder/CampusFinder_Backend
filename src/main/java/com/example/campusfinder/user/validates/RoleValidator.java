package com.example.campusfinder.user.validates;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * packageName    : com.example.campusfinder.user.validates
 * fileName       : RoleValidator
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Component
public class RoleValidator implements Validator<String>{
    private static final Set<String> VALID_ROLES = Set.of("STUDENT","PROFESSOR");

    @Override
    public void validate(String role){
        if(role == null || !VALID_ROLES.contains(role.toUpperCase())){
            throw new IllegalArgumentException("유효하지 않은 역할입니다.");
        }
    }
}
