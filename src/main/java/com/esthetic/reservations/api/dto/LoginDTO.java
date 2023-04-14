package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "El nombre de usuario es requerido.")
    private String username;

    @NotBlank(message = "La contraseña es requerida.")
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
