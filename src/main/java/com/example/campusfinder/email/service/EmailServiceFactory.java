package com.example.campusfinder.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.campusfinder.email.service
 * fileName       : EmailServiceFactory
 * author         : tlswl
 * date           : 2024-09-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-28        tlswl       최초 생성
 */
@Component
@RequiredArgsConstructor
public class EmailServiceFactory {
    private final StudentEmailService studentEmailService;
    private final ProfessorEmailService professorEmailService;

    public EmailService getService(String role){
        if("PROFESSOR".equalsIgnoreCase(role)){
            return professorEmailService;
        }
        return studentEmailService;
    }
}
