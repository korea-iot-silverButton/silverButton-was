package com.korit.silverbutton.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public abstract class MailService {

    public abstract MimeMessage createMail(String mail, String token) throws MessagingException;

    public String sendSimpleMessage(String sendEmail, String username) throws MessagingException {
        return "이메일 전송 로직이 구현되지 않았습니다.";
    }

    public String verifyEmail(String token) {

        return "이메일 인증이 처리되지 않았습니다.";
    }

}
