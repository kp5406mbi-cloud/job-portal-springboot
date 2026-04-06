package com.jobportal.controller;

import com.jobportal.entity.Users;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 🔹 Show registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // 🔹 Handle registration
    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role,
                           Model model) {

        try {
            // Debug logs (optional)
            System.out.println("EMAIL: " + email);
            System.out.println("PASSWORD: " + password);
            System.out.println("ROLE: " + role);

            // Create user object
            Users user = new Users();
            user.setEmail(email);
            user.setPassword(password); // ✅ DO NOT encode here
            user.setRole(role);

            // Save user (encoding handled in service)
            userService.saveUser(user);

            return "redirect:/login";

        } catch (RuntimeException e) {
            // ✅ Handle duplicate email or other errors
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
