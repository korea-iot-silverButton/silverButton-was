package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.Mail.SendMailRequestDto;
import com.korit.silverbutton.service.AuthService;
import com.korit.silverbutton.service.MailService;
import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.MAIL)
public class MailController {

    private final MailService mailService;
    private final AuthService authService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody SendMailRequestDto mailDto) throws MessagingException {
        System.out.println(mailDto);
        return mailService.sendSimpleMessage(mailDto.getEmail(), mailDto.getName());
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        System.out.println("verify");
        return mailService.verifyEmail(token);
    }


}