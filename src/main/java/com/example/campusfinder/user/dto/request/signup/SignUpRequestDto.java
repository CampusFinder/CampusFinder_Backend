package com.example.campusfinder.user.dto.request.signup;

import com.example.campusfinder.user.dto.request.element.*;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * packageName    : com.example.campusfinder.user.dto.request.signup
 * fileName       : SignUpRequestDto
 * author         : tlswl
 * date           : 2024-08-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-30        tlswl       최초 생성
 */
public record SignUpRequestDto(
        RoleDto role,
        EmailDto email,
        PhoneNumberDto phoneNum,
        PasswordDto password,
        NicknameDto nickname,
        UniversityDto univName
) {
    public SignUpRequestDto {
        if (role == null) {
            throw new IllegalArgumentException("Role은 null일 수 없습니다.");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email은 null일 수 없습니다.");
        }
        if (phoneNum == null) {
            throw new IllegalArgumentException("Phone number는 null일 수 없습니다.");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password는 null일 수 없습니다.");
        }
        if (nickname == null) {
            throw new IllegalArgumentException("Nickname은 null일 수 없습니다.");
        }
        if(univName == null){
            throw new IllegalArgumentException("학교 이름은 null일 수 없습니다.");
        }

        // 각 필드에서 추가적인 유효성 검사가 필요하다면, 이곳에서 추가 가능
        // 예를 들어, Role의 유효성 검사
        role = new RoleDto(role.role());
        email = new EmailDto(email.email());
        phoneNum = new PhoneNumberDto(phoneNum.phoneNumber());
        password = new PasswordDto(password.password());
        nickname = new NicknameDto(nickname.nickname());
        univName = new UniversityDto(univName.univName());
    }
}
