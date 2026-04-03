package com.jobportal.dto;

public class UserDTO {

    private Long userId;
    private String email;
    private String password;
    private String role;

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

    public String getRole() {
        return role;
    }
}