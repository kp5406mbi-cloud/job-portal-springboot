package com.jobportal.controller;

import com.jobportal.dto.UserDTO;
import com.jobportal.entity.Users;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    private String role;

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role) {

        System.out.println("EMAIL: " + email);
        System.out.println("PASSWORD: " + password);
        System.out.println("ROLE: " + role);

        Users user = new Users();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userService.saveUser(user);

        return "redirect:/login";
    }


    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/clear-users")
    public String clearUsers() {
        userService.deleteAllUsers();
        return "redirect:/login";
    }
}
