package com.jobportal.dto;

public class UserDTO {

    private Long userId;
    private String email;
    private String password;

    public UserDTO(Long userId, String email) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}