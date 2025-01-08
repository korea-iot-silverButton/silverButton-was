package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.provider.JwtProvider;
import com.korit.silverbutton.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl extends MailService {

    private final JavaMailSender javaMailSender;
    private final JwtProvider jwtProvider;

    @Value("${spring.mail.username}")
    private String senderEmail;  // 발신자 이메일 설정 (application.properties에서 읽어옴)

    // 이메일 생성 메서드 (HTML 형식으로 이메일을 생성)
    @Override
    public MimeMessage createMail(String mail, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail);  // 발신자 이메일 주소 설정
        message.setRecipients(MimeMessage.RecipientType.TO, mail);  // 수신자 이메일 설정
        message.setSubject("이메일 인증 링크");

        // 이메일 본문에 HTML 형식으로 인증 링크 추가
        String body = "<h3>이메일 인증 링크입니다.</h3>" +
                "<a href=\"http://localhost:4040/api/v1/mail/verify?token=" + token + "\">여기를 클릭하여 인증하세요</a>" +
                "<p>감사합니다.</p>";

        message.setText(body, "UTF-8", "html");  // UTF-8 문자 인코딩 및 HTML 형식 설정
        return message;
    }

    // 인증 메일 발송 메서드 (사용자에게 인증 이메일을 전송)
    @Override
    public String sendSimpleMessage(String sendEmail, String userId) throws MessagingException {
        // JWT 토큰 생성 (이메일 인증을 위한 유효한 토큰 생성)
        String token = jwtProvider.generateEmailValidToken(userId);

        // 인증 메일 생성
        MimeMessage message = createMail(sendEmail, token);

        try {
            // 메일 발송
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return "메일 발송 중 오류가 발생하였습니다.";  // 메일 발송 실패 시 오류 메시지 반환
        }

        return token;  // 인증 토큰 반환
    }

    // 이메일 인증 메서드 (토큰을 검증하고 인증 완료 처리)
    @Override
    public String verifyEmail(String token) {
        try {
            // JWT 토큰에서 사용자 ID 추출
            String userId = jwtProvider.getUsernameFromEmailJwt(token);
            System.out.println("이메일 인증이 완료되었습니다: " + userId);
            return userId;  // 인증 완료된 사용자 ID 반환
        } catch (Exception e) {
            return "토큰이 만료되었습니다. 다시 진행해주세요.";  // 토큰 만료 시 오류 메시지 반환
        }
    }
}
