package com.jobportal.service;

import com.jobportal.entity.Users;
import com.jobportal.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        System.out.println("CUSTOM OAUTH SERVICE CALLED");

        OAuth2User oauthUser = super.loadUser(request);

        String email = oauthUser.getAttribute("email");

        System.out.println("GOOGLE EMAIL = " + email);

        System.out.println(
                "USER EXISTS = " +
                        userRepository.findByEmail(email).isPresent()
        );

        userRepository.findByEmail(email)
                .orElseGet(() -> {

                    System.out.println("CREATING USER");

                    Users user = new Users();

                    user.setEmail(email);
                    user.setPassword(UUID.randomUUID().toString());
                    user.setRole("PENDING");

                    Users saved = userRepository.save(user);

                    System.out.println("SAVED USER ID = " + saved.getUserId());

                    return saved;
                });

        return oauthUser;
    }

}