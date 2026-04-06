package com.jobportal.service;

import com.jobportal.entity.Users;
import com.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.security.cert.Extension;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;


 /*   public Users saveUser(Users user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword())); // direct
        return userRepository.save(user);
    }  */


    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole()) // IMPORTANT
                .build();
    }

    public void saveUser(Users user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        if (user.getPassword() == null) {
            throw new RuntimeException("Password is null - check form binding");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


}





