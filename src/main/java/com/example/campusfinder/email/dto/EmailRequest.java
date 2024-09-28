package com.example.campusfinder.email.dto;

import com.example.campusfinder.user.entity.Role;

/**
 * packageName    : com.example.campusfinder.email.dto
 * fileName       : EmailRequest
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */

public record EmailRequest(
        Role role,
        String univName,
        String email
) {
    public EmailRequest {
        if (univName == null || univName.isBlank()) {
            throw new IllegalArgumentException("대학교 이름은 빈칸일 수 없습니다.");
        }
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }
        if (role == null) {
            throw new IllegalArgumentException("role을 입력해주세요.");
        }
    }
}
