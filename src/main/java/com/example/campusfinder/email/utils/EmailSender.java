package com.example.campusfinder.email.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;

/**
 * packageName    : com.example.campusfinder.email.utils
 * fileName       : EmailSender
 * author         : tlswl
 * date           : 2024-09-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-23        tlswl       최초 생성
 */
@Component
@RequiredArgsConstructor
public class EmailSender {

    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);
    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);  // 이 부분이 받는 이메일을 설정하는 부분입니다.
            helper.setSubject(subject);
            helper.setText(text);

            // 발신자 이메일과 발신자 이름 설정
            helper.setFrom("zaq230423@gmail.com", "CampusFinder");

            // 이메일 발송
            logger.info("Sending email to {}", to);  // to 값이 받는 이메일입니다.
            mailSender.send(message);
            logger.info("Email sent successfully to {}", to);  // 성공 로그
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new IllegalStateException("이메일 발송에 실패했습니다.", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
