package com.korit.silverbutton.controller;

import com.korit.silverbutton.dto.Mail.SendMailRequestDto;
import com.korit.silverbutton.service.MailService;
import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MailController {

    @Autowired
    private final MailService mailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody SendMailRequestDto mailDto) throws MessagingException {
        return mailService.sendSimpleMessage(mailDto.getEmail(), mailDto.getUserId());
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        return mailService.verifyEmail(token);
    }
}