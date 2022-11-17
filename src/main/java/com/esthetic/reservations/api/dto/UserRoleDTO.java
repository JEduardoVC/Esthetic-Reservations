package com.esthetic.reservations.api.dto;

import com.esthetic.reservations.api.model.Role;
import com.esthetic.reservations.api.model.UserEntity;

public class UserRoleDTO {

    private Long userRoleId;

    private Role role;

    private UserEntity user;

    public UserRoleDTO() {
    }

    public Long getUserRoleId() {
        return this.userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
