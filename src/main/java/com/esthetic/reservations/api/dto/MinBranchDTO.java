package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MinBranchDTO extends GenericModelDTO {

    @NotNull
    @NotEmpty
    private String branch_name;

    private String location;

    @NotNull
    private Long ownerId;

    public MinBranchDTO() {
    }

    public MinBranchDTO(Long id, String branch_name, String location, Long ownerId) {
        super(id);
        this.branch_name = branch_name;
        this.location = location;
        this.ownerId = ownerId;
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

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

}
