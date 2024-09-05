package com.example.campusfinder.user.validates;

import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.campusfinder.user.validates
 * fileName       : VerifyEmailValidator
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Component
public class EmailValidator implements Validator<String>{

    @Override
    public void validate(String email){
        if(email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }
    }
}
