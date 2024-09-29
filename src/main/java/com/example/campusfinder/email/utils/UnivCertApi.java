package com.example.campusfinder.email.utils;

import com.example.campusfinder.email.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.univcert.api.UnivCert.*;

@Service
public class UnivCertApi {

    @Value("${univcert.api-key}")
    private String univCertApiKey;

    // 인증 요청 메서드
    public String requestCertification(EmailRequest emailRequest) throws IOException {
        Map<String, Object> response = certify(univCertApiKey, emailRequest.email(), emailRequest.univName(), true);

        // 응답에서 인증 성공 여부 확인
        if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
            throw new IllegalStateException("인증 요청이 실패했습니다. API 응답: " + response);
        }

        // 인증번호가 응답에 포함된 경우 반환
        return response.get("code").toString();  // 인증번호가 포함된다고 가정
    }

    // 인증 코드 검증 메서드
    public boolean verifyCertificationCode(String email, String univName, int code, String apiKey) throws IOException {
        Map<String, Object> response = certifyCode(apiKey, email, univName, code);

        if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
            throw new IllegalStateException("인증 코드 검증이 실패했습니다. API 응답: " + response);
        }
        return true;
    }

    // UnivCert API 상태 초기화 메서드 (요청을 취소하고 초기화)
    public void clearCertification(String email) throws IOException {
        Map<String, Object> response = clear(univCertApiKey, email);

        if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
            throw new IllegalStateException("유저 인증 초기화가 실패했습니다. API 응답: " + response);
        }
    }

    // 이메일 인증 상태 확인 메서드
    public boolean checkEmailCertificationStatus(String email) throws IOException {
        Map<String, Object> response = status(univCertApiKey, email);

        if (response == null) {
            throw new IllegalStateException("인증 상태 확인 실패: API 응답이 없습니다.");
        }

        return Boolean.TRUE.equals(response.get("success"));
    }
}