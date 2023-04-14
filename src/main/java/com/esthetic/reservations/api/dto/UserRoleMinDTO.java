package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotNull;

public class UserRoleMinDTO {

    @NotNull(message = "El ID del usuario es requerido.")
    private Long userId;

    @NotNull(message = "El ID del rol es requerido.")
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
