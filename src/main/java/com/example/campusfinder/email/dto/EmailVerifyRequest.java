package com.example.campusfinder.email.dto;

/**
 * packageName    : com.example.campusfinder.email.dto
 * fileName       : EmailVerifyRequest
 * author         : tlswl
 * date           : 2024-08-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-31        tlswl       최초 생성
 */
public record EmailVerifyRequest(String email, String univName, int code) {
    public EmailVerifyRequest{
        if(email==null || email.isBlank()){
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
        if(univName == null || univName.isBlank()){
            throw new IllegalArgumentException("대학 이름을 입력해주세요");
        }
        if(code<=0){
            throw new IllegalArgumentException("인증코드를 입력해주세요");
        }
    }
}
