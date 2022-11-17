package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotNull;

public class UserRoleMinDTO {

    @NotNull
    private Long userId;

    @NotNull
    private Long roleId;

    public UserRoleMinDTO() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
