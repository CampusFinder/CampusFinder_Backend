package com.example.campusfinder.user.validates;

import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.campusfinder.user.validates
 * fileName       : SchoolValidator
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Component
public class UniversityValidator implements Validator<String>{
    @Override
    public void validate(String univName) {
        if (univName == null || univName.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 학교 이름입니다.");
        }
    }
}
