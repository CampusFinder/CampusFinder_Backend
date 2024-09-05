package com.example.campusfinder.user.validates;

/**
 * packageName    : com.example.campusfinder.user.validates
 * fileName       : Validator
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public interface Validator<T> {
    void validate(T target);
}
