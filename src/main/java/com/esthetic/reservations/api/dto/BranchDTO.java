package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.esthetic.reservations.api.model.UserEntity;

public class BranchDTO extends GenericModelDTO {

    @NotNull
    @NotEmpty
    private String branch_name;

    private String location;

    @NotNull
    private UserEntity owner;

    public BranchDTO() {
    }

    public BranchDTO(String branch_name, String location, UserEntity owner) {
        this.branch_name = branch_name;
        this.location = location;
        this.owner = owner;
    }

    public BranchDTO(Long id, String branch_name, String location, UserEntity owner) {
        super(id);
        this.branch_name = branch_name;
        this.location = location;
        this.owner = owner;
    }

    public String getBranchName() {
        return this.branch_name;
    }

    public void setBranchName(String branch_name) {
        this.branch_name = branch_name;
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