package com.esthetic.reservations.api.dto;

public class RoleDTO {

    private Long roleId;

    private String name;

    public RoleDTO() {
    }

    public RoleDTO(Long roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
