package com.esthetic.reservations.api.dto;

import java.util.ArrayList;
import java.util.List;

public class UserEntityEditRolesDTO extends UserEntityEditDTO {

    private List<Long> rolesIds;

    private List<Long> workingBranchesIds;

    public UserEntityEditRolesDTO() {
        super();
        this.rolesIds = new ArrayList<>();
        this.workingBranchesIds = new ArrayList<>();
    }

    public UserEntityEditRolesDTO(List<Long> rolesIds, List<Long> workingBranchesIds) {
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
