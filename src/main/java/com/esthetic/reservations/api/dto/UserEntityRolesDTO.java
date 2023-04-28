package com.esthetic.reservations.api.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserEntityRolesDTO extends UserEntityDTO {

    @NotNull(message = "Se requieren los roles del usuario.")
    @NotEmpty(message = "Se requieren los roles del usuario.")
    private List<Long> rolesIds;

    private List<Long> workingBranchesIds;

    public UserEntityRolesDTO() {
        super();
        this.rolesIds = new ArrayList<>();
        this.workingBranchesIds = new ArrayList<>();
    }

    public UserEntityRolesDTO(List<Long> rolesIds, List<Long> workingBranchesIds) {
        super();
        this.rolesIds = rolesIds;
        this.workingBranchesIds = workingBranchesIds;
    }

    public List<Long> getRolesIds() {
        return this.rolesIds;
    }

    public void setRolesIds(List<Long> rolesIds) {
        this.rolesIds = rolesIds;
    }

    public List<Long> getWorkingBranchesIds() {
        return this.workingBranchesIds;
    }

    public void setWorkingBranchesIds(List<Long> workingBranchesIds) {
        this.workingBranchesIds = workingBranchesIds;
    }

}
