package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotBlank;

public class RoleDTO extends GenericModelDTO {

    @NotBlank(message = "Se requiere el nombre del rol.")
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
