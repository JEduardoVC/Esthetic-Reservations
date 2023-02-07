package com.esthetic.reservations.api.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.esthetic.reservations.api.model.Role;

public class MinUserEntityDTO extends GenericModelDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$", message = "El nombre de usuario debe tener al menos 3 letras y/o números sin símbolos especiales.")
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotEmpty
    @Size(max = 10, min = 10, message = "El número de teléfono tiene que tener 10 dígitos")
    private String phoneNumber;

    @NotBlank
    private String address;

    @Email(message = "Correo electrónico no válido", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotBlank
    private String email;

    @Pattern(regexp = "\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[@#$%^&+=!])\\S{8,}\\z", message = "Contraseña inválida. La contraseña debe contener al menos 8 caracteres, un número, una mayúsucula y un símbolo especial (@#$%^&+=!)")
    @NotBlank
    private String password;

    private List<Role> userRoles;

    public MinUserEntityDTO() {
        super();
    }

    public MinUserEntityDTO(Long id, String username, String name, String lastName, String phoneNumber, String address,
            String email, String password, List<Role> userRoles) {
        super(id);
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }

}
