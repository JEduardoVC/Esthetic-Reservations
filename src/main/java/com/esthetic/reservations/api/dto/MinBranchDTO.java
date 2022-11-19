package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MinBranchDTO extends GenericModelDTO {

    @NotNull
    @NotEmpty
    private String name;

    private String location;

    @NotNull
    private Long ownerId;

    public MinBranchDTO() {
    }

    public MinBranchDTO(Long id, String name, String location, Long ownerId) {
        super(id);
        this.name = name;
        this.location = location;
        this.ownerId = ownerId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

}
