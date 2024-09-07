package com.example.campusfinder.user.dto.request.signin;

/**
 * packageName    : com.example.campusfinder.user.dto.request.signin
 * fileName       : SignInRequsetDto
 * author         : tlswl
 * date           : 2024-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-07        tlswl       최초 생성
 */
public record SignInRequestDto(String phone, String password)
{
    public SignInRequestDto{
        if(phone == null || phone.isBlank()){
            throw new IllegalArgumentException("전화번호를 필수로 입력해주세요.");
        }
        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("비밀번호를 필수로 입력해주세요.");
        }

    }
}
