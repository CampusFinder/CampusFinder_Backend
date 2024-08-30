package com.example.campusfinder.email.repository;

import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * packageName    : com.example.campusfinder.email.repository
 * fileName       : EmailVerificationRepository
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Repository
public class EmailVerificationRepository {

    private final Set<String> verifiedEmails = new HashSet<>();

    public void saveVerifiedEmail(String email) {
        verifiedEmails.add(email);
    }

    public boolean isEmailVerified(String email) {
        return verifiedEmails.contains(email);
    }
}
