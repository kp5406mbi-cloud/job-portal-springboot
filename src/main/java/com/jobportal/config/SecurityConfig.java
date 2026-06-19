package com.jobportal.config;

import com.jobportal.entity.Users;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import com.jobportal.service.CustomOAuth2UserService;

@Configuration
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;

    public SecurityConfig(
            CustomOAuth2UserService customOAuth2UserService,
            UserRepository userRepository) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.userRepository = userRepository;
    }




    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService,
                                                         BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


    @Bean
    public  BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {

        http
                .authenticationProvider(authenticationProvider)

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/login", "/register", "/css/**").permitAll()
                        .requestMatchers("/jobs", "/jobs/**").permitAll()
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/choose-role",
                                "/css/**",
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        .requestMatchers("/recruiter/**").hasRole("RECRUITER")
                        .requestMatchers("/apply/**").hasRole("USER")

                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )

                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService))
                        .successHandler(successHandler())
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );


        return http.build();
    }


    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {





            System.out.println("AUTHORITIES:");

            authentication.getAuthorities()
                    .forEach(a -> System.out.println(a.getAuthority()));

            String email = null;

            if(authentication.getPrincipal() instanceof OAuth2User oauthUser){
                email = oauthUser.getAttribute("email");
            }

            if(email != null){

                Users user =
                        userRepository.findByEmail(email).orElse(null);

                if(user != null &&
                        "PENDING".equals(user.getRole())) {

                    response.sendRedirect("/choose-role");
                    return;
                }

                if(user != null &&
                        "RECRUITER".equals(user.getRole())) {

                    response.sendRedirect("/recruiter/dashboard");
                    return;
                }
            }

            response.sendRedirect("/user/jobs");
        };
    }

}

/*package com.jobportal.config;

import com.jobportal.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationProvider authenticationProvider) throws Exception {

        http
                .authenticationProvider(authenticationProvider)

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/login", "/register", "/css/**").permitAll()
                        .requestMatchers("/jobs", "/jobs/**").permitAll()
                        .requestMatchers("/", "/api/**").permitAll()
                        .requestMatchers("/", "/health", "/api/**").permitAll()

                        // ROLE BASED
                        .requestMatchers("/recruiter/**").hasRole("RECRUITER")
                        .requestMatchers("/apply/**").hasRole("USER")

                        // FINAL
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api")) {
                                response.setStatus(401);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\": \"Unauthorized\"}");
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {

            var authorities = authentication.getAuthorities();

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"))) {
                response.sendRedirect("/recruiter/dashboard");
            } else {
                response.sendRedirect("/user/jobs");
            }
        };
    }
}   */
