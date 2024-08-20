package com.example.campusfinder.user.validates;

import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.campusfinder.user.validates
 * fileName       : VerifyPhoneNmberValidator
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Component
public class VerifyPhoneNmberValidator implements Validator<String>{

    @Override
    public void validate(String phoneNumber){
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("핸드폰 번호는 빈칸일 수 없습니다.");
        }
        if(phoneNumber == null || !phoneNumber.matches("^\\d{10,11}$")){
            throw new IllegalArgumentException("유효하지 않은 핸드폰 번호입니다.");
        }
    }
}
