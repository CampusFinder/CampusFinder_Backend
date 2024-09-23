package com.example.campusfinder.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.email.config
 * fileName       : ProfessorEmailConfig
 * author         : tlswl
 * date           : 2024-09-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-23        tlswl       최초 생성
 */
@Component
@ConfigurationProperties(prefix = "professor")
public class ProfessorEmailConfig {
    private List<String> allowedDomains;

    public List<String> getAllowedDomains(){
        return allowedDomains;
    }

    public void setAllowedDomains(List<String> allowedDomains){
        this.allowedDomains=allowedDomains;
    }
}
