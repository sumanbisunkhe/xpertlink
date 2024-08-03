package com.example.xpertlink.service.impl;

import com.example.xpertlink.dto.EmailDto;
import com.example.xpertlink.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("readymade090@gmail.com");
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getMessage());
        message.setTo(emailDto.getTo());
        javaMailSender.send(message);

    }
}