package com.example.campusfinder.sms.utils;

/**
 * packageName    : com.example.campusfinder.sms.utils
 * fileName       : SmsSender
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
public interface SmsSender {
    void sendSms(String to, String message);
}
