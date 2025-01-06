package com.korit.silverbutton.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

public interface JavaMailSender extends MailSender {
    MimeMessage createMimeMessage();

    MimeMessage createMimeMessage(InputStream contentStream) throws MailException;

    default void send(MimeMessage mimeMessage) throws MailException {
        this.send(mimeMessage);
    }

    void send(MimeMessage... mimeMessages) throws MailException;

    default void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        this.send(mimeMessagePreparator);
    }

    default void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
        try {
            List<MimeMessage> mimeMessages = new ArrayList(mimeMessagePreparators.length);
            MimeMessagePreparator[] var3 = mimeMessagePreparators;
            int var4 = mimeMessagePreparators.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                MimeMessagePreparator preparator = var3[var5];
                MimeMessage mimeMessage = this.createMimeMessage();
                preparator.prepare(mimeMessage);
                mimeMessages.add(mimeMessage);
            }

            this.send((MimeMessage[])mimeMessages.toArray(new MimeMessage[0]));
        } catch (MailException var8) {
            throw var8;
        } catch (MessagingException var9) {
            throw new MailParseException(var9);
        } catch (Exception var10) {
            Exception ex = var10;
            throw new MailPreparationException(ex);
        }
    }
}
