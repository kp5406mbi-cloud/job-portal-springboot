package com.jobportal.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @PostConstruct
    public void init() {
        System.out.println("========== MAIL CONFIG ==========");
        System.out.println("MAIL USER = " + sender);
        System.out.println("MAIL HOST = " + host);
        System.out.println("MAIL PORT = " + port);
        System.out.println("=================================");
    }

    public void sendEmail(String to, String subject, String body) {

        System.out.println("========== EMAIL SERVICE ==========");
        System.out.println("Sender   : " + sender);
        System.out.println("Receiver : " + to);
        System.out.println("Subject  : " + subject);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        System.out.println("Calling mailSender.send()...");

        mailSender.send(message);

        System.out.println("EMAIL SENT SUCCESSFULLY");
        System.out.println("==================================");
    }

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;


}