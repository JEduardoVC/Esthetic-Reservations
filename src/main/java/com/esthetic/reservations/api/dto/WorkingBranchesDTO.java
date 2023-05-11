package com.esthetic.reservations.api.dto;

import java.util.List;

public class WorkingBranchesDTO {

    private List<Long> branchesIds;

    public WorkingBranchesDTO() {
    }


    public WorkingBranchesDTO(List<Long> branchesIds) {
        this.branchesIds = branchesIds;
    }

    public List<Long> getBranchesIds() {
        return this.branchesIds;
    }

    public void setBranchesIds(List<Long> branchesIds) {
        this.branchesIds = branchesIds;
    }


}
