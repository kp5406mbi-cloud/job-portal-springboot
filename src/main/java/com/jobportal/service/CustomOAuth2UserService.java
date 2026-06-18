package com.jobportal.service;

import com.jobportal.entity.Users;
import com.jobportal.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService
        extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser =
                super.loadUser(request);

        String email =
                oauthUser.getAttribute("email");

        userRepository.findByEmail(email)
                .orElseGet(() -> {

                    Users user = new Users();

                    user.setEmail(email);

                    user.setPassword("");

                    user.setRole("USER");

                    return userRepository.save(user);
                });

        return oauthUser;
    }
}