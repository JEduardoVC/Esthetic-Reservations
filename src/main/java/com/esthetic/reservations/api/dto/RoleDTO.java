package com.esthetic.reservations.api.dto;

public class RoleDTO extends GenericModelDTO {

    private String name;

    public RoleDTO() {
    }

    public RoleDTO(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
