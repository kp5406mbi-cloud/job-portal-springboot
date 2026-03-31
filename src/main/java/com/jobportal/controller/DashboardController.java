package com.jobportal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DashboardController {

    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return "user-dashboard";
    }

    @GetMapping("/recruiter/dashboard")
    public String recruiterDashboard() {
        return "recruiter-dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"))) {
            return "redirect:/recruiter/dashboard";
        }
        return "redirect:/user/dashboard";
    }
}
