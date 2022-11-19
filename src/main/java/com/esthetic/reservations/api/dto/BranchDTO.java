package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.esthetic.reservations.api.model.UserEntity;

public class BranchDTO extends GenericModelDTO {

    @NotNull
    @NotEmpty
    private String name;

    private String location;

    @NotNull
    private UserEntity owner;

    public BranchDTO() {
    }

    public BranchDTO(String name, String location, UserEntity owner) {
        this.name = name;
        this.location = location;
        this.owner = owner;
    }

    public BranchDTO(Long id, String name, String location, UserEntity owner) {
        super(id);
        this.name = name;
        this.location = location;
        this.owner = owner;
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

    public UserEntity getOwner() {
        return this.owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

}