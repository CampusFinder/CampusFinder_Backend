package com.example.campusfinder.email.service;

import com.example.campusfinder.email.dto.EmailRequest;

import java.io.IOException;

/**
 * packageName    : com.example.campusfinder.email.service
 * fileName       : qwe
 * author         : tlswl
 * date           : 2024-09-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-28        tlswl       최초 생성
 */
public interface EmailService {
    void sendVerificationCode(EmailRequest emailRequest) throws IOException;
    boolean verifyCode(String email, String univName, int code) throws IOException;
    void clearPendingVerification(String email) throws IOException;
    boolean isVerificationPending(String email);
    boolean isValidEmailFormat(String email);
}
