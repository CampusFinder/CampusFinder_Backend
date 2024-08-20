package com.example.campusfinder.user.dto.request;


/**
 * packageName    : com.example.campusfinder.user.dto.request
 * fileName       : UserRequest
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public record UserRequest(
        String phoneNumber,
        String password,
        String role,
        String school,
        String email,
        String nickName
)
{ }
