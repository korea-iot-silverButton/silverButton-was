package com.korit.silverbutton.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public abstract class MailService {
//
//    MimeMessage createMail(String mail, String token) throws MessagingException {
//        return null;
//    }

//    public abstract MimeMessage createMail(String mail, String token) throws MessagingException;

    public abstract MimeMessage createMail(String mail, String token) throws MessagingException;

    public String sendSimpleMessage(String sendEmail, String username) throws MessagingException {
        return null;
    }

    public String verifyEmail(String token) {
        return null;
    }
}
