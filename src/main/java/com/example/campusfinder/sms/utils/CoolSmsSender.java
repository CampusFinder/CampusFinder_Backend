package com.example.campusfinder.sms.utils;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import net.nurigo.sdk.message.service.MessageHttpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.example.campusfinder.sms.utils
 * fileName       : CoolSmsSender
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Component
public class CoolSmsSender implements SmsSender{

    private final DefaultMessageService messageService;
    private final String senderNumber;

    public CoolSmsSender(
            @Value("${spring.coolsms.apiKey}") String apiKey,
            @Value("${spring.coolsms.apiSecret}") String apiSecret,
            @Value("${spring.coolsms.senderNumber}") String senderNumber
    ){
        this.senderNumber = senderNumber;
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    @Override
    public void sendSms(String to, String message){
        Message smsMessage = new Message();
        smsMessage.setFrom(senderNumber);
        smsMessage.setTo(to);
        smsMessage.setText(message);

        messageService.sendOne(new SingleMessageSendingRequest(smsMessage));
    }
}
