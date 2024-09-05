package com.example.campusfinder.email.dto;

import com.example.campusfinder.user.entity.Role;

public record EmailVerifyRequest(String email, String univName, int code, Role role) {
    public EmailVerifyRequest {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
        if (role == null) {
            throw new IllegalArgumentException("역할을 입력해주세요.");
        }
        if (univName == null || univName.isBlank()) {
            throw new IllegalArgumentException("대학 이름을 입력해주세요.");
        }
        if (code <= 0) {
            throw new IllegalArgumentException("인증 코드를 입력해주세요.");
        }
    }
}
