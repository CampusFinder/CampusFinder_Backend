package com.example.campusfinder.email.utils;

import com.example.campusfinder.email.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.univcert.api.UnivCert.*;
/**
 * packageName    : com.example.campusfinder.email.utils
 * fileName       : UnivCertApi
 * author         : tlswl
 * date           : 2024-09-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-28        tlswl       최초 생성
 */
@Service
public class UnivCertApi {
    @Value("${univcert.api.key}")
    private String univCertApiKey;

    public void requestCertification(EmailRequest emailRequest) throws IOException {
        certify(univCertApiKey, emailRequest.email(), emailRequest.univName(), true);
    }

    public boolean verifyCertificationCode(String email, String univName, int code, String apiKey) throws IOException {
        Map<String, Object> response = certifyCode(apiKey, email, univName, code);
        return (boolean) response.get("success");
    }

    public void clearCertification(String email) throws IOException {
        clear(univCertApiKey, email);
    }
}
