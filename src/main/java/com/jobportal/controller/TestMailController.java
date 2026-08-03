package com.jobportal.controller;

import com.jobportal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class TestMailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test")
    public String sendTestMail() {

        emailService.sendEmail(
                "kp5406.mbi@gmail.com",
                "Job Portal Test",
                "Congratulations! Email notifications are working."
        );

        return "Mail Sent";
    }
}