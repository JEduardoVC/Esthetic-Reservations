package com.esthetic.reservations.api.dto;

import java.util.List;

import com.esthetic.reservations.api.model.Role;

public class LoginResponseDTO {

    private String token;

    private List<Role> userRoles;

    private Long userId;

    public LoginResponseDTO(String token, List<Role> userRoles, Long userId) {
        this.token = token;
        this.userRoles = userRoles;
        this.userId = userId;
    }

    public LoginResponseDTO() {
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Role> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
