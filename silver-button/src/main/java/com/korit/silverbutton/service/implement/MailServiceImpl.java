package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.provider.JwtProvider;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailServiceImpl extends MailService {

    private final JavaMailSender javaMailSender;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public MimeMessage createMail(String mail, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("이메일 인증 링크");

        String body = "<h3>이메일 인증 링크입니다.</h3>" +
                "<a href=\"http://localhost:3000/verifyId?token=" + token + "\">여기를 클릭하여 인증하세요</a>" +
                "<p>감사합니다.</p>";

        message.setText(body, "UTF-8", "html");
        return message;
    }

    @Override
    public String sendSimpleMessage(String sendEmail, String username) throws MessagingException {

        Optional<User> optionalUser;
        System.out.println(username);
        System.out.println("sendSimpleMessage1");
        try {
            System.out.println("sendEmail: " + sendEmail + ", username: " + username); // 값 확인
            optionalUser = userRepository.findUserByEmailAndName(sendEmail, username);
            System.out.println("Optional<User>: " + optionalUser); // Optional<User> 내용 확인
            if (optionalUser.isEmpty()) {
                return "해당 이름과 이메일로 등록된 사용자를 찾을 수 없습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "사용자 조회 중 오류가 발생하였습니다.";
        }

        System.out.println("sendSimpleMessage3");

        User user = optionalUser.get();

        System.out.println("sendSimpleMessage4");

        String token;
        try {
            token = jwtProvider.generateEmailValidToken(user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return "토큰 생성 중 오류가 발생하였습니다.";
        }
        System.out.println("sendSimpleMessage5");
        try {
            MimeMessage message = createMail(sendEmail, token);
            System.out.println("sendSimpleMessage6");
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return "메일 발송 중 오류가 발생하였습니다.";
        }
        return token;
    }

    @Override
    public String verifyEmail(String token) {
        try {
            System.out.println("token" + token);
            String username = jwtProvider.getUsernameFromEmailJwt(token);
            System.out.println("이메일 인증이 완료되었습니다: " + username);
            return username;
        } catch (Exception e) {
            return "토큰이 만료되었습니다. 다시 진행해주세요.";
        }
    }
}
