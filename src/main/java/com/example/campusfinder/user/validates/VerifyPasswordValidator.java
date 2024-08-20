package com.example.campusfinder.user.validates;

import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.campusfinder.user.validates
 * fileName       : VerifyPasswordValidator
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Component
public class VerifyPasswordValidator implements Validator<String>{

    @Override
    public void validate(String password){
        if(password == null || password.length()<4){
            throw new IllegalArgumentException("비밀번호는 최소 4자리 이상이어야 합니다.");
        }
    }
}
