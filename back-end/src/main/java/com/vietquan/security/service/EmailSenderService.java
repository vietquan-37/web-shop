package com.vietquan.security.service;

import com.vietquan.security.request.MailForOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void setMailSender(MailForOrderRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bubakush20099@gmail.com");
        message.setTo(request.getToEmail());
        message.setSubject(request.getSubject());
        message.setText(request.getBody());
        mailSender.send(message);
    }
}
