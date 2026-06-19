package com.jobportal.controller;

import com.jobportal.entity.Users;
import com.jobportal.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RoleController {

    private final UserRepository userRepository;

    public RoleController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/choose-role")
    public String chooseRole() {
        return "choose-role";
    }

    @PostMapping("/choose-role")
    public String saveRole(
            @RequestParam String role,
            Authentication authentication
    ) {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email =
                oauthUser.getAttribute("email");

        Users user =
                userRepository.findByEmail(email).orElseThrow();

        user.setRole(role);

        userRepository.save(user);

        if(role.equals("RECRUITER")){
            return "redirect:/recruiter/dashboard";
        }

        return "redirect:/user/jobs";
    }
}