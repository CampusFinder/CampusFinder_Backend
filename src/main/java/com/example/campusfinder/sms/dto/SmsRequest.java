package com.example.campusfinder.sms.dto;

/**
 * packageName    : com.example.campusfinder.sms.dto
 * fileName       : SmsRequest
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public record SmsRequest(String phoneNumber, String code, String email) {

    // 정적 팩토리 메서드: sms 전송을 위한 요청 객체 생성
    public static SmsRequest createForSending(String phoneNum, String email) {
        if (phoneNum == null || phoneNum.isBlank()) {
            throw new IllegalArgumentException("핸드폰번호는 빈칸일 수 없습니다");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 빈칸일 수 없습니다");
        }
        return new SmsRequest(phoneNum, null, email);
    }

    // 정적 팩토리 메서드: 인증을 위한 요청 객체 생성
    public static SmsRequest createForVerification(String phoneNumber, String certificationNumber) {
        if (phoneNumber == null || phoneNumber.isBlank() || certificationNumber == null || certificationNumber.isBlank()) {
            throw new IllegalArgumentException("핸드폰 번호와 인증번호는 빈칸일 수 없습니다");
        }
        return new SmsRequest(phoneNumber, certificationNumber, null);
    }
}
