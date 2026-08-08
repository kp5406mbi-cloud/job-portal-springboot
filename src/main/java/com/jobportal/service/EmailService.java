package com.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmail(String to, String subject, String body) {

        System.out.println("========== BREVO EMAIL ==========");
        System.out.println("Sender   : " + sender);
        System.out.println("Receiver : " + to);
        System.out.println("Subject  : " + subject);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> request = Map.of(
                "sender", Map.of(
                        "email", sender,
                        "name", "Job Portal"
                ),
                "to", List.of(
                        Map.of("email", to)
                ),
                "subject", subject,
                "textContent", body
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(request, headers);

        restTemplate.postForEntity(
                "https://api.brevo.com/v3/smtp/email",
                entity,
                String.class
        );

        System.out.println("EMAIL SENT SUCCESSFULLY");
        System.out.println("===============================");
    }

    public void sendHtmlEmail(
            String to,
            String subject,
            String html
    ) {

        System.out.println("========== BREVO HTML EMAIL ==========");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> request = Map.of(

                "sender",
                Map.of(
                        "email", sender,
                        "name", "Job Portal"
                ),

                "to",
                List.of(
                        Map.of("email", to)
                ),

                "subject",
                subject,

                "htmlContent",
                html
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(request, headers);

        restTemplate.postForEntity(
                "https://api.brevo.com/v3/smtp/email",
                entity,
                String.class
        );

        System.out.println("HTML EMAIL SENT SUCCESSFULLY");
    }
}