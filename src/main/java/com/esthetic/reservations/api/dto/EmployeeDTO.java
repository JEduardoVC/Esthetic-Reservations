package com.esthetic.reservations.api.dto;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.esthetic.reservations.api.model.Role;

public class EmployeeDTO extends GenericModelDTO {

    private UserEntityDTO user;

    private Set<MinBranchDTO> workingBranches;


    public EmployeeDTO() {
    }


    public EmployeeDTO(UserEntityDTO user, Set<MinBranchDTO> workingBranches) {
        this.user = user;
        this.workingBranches = workingBranches;
    }

    public UserEntityDTO getUser() {
        return this.user;
    }

    public void setUser(UserEntityDTO user) {
        this.user = user;
    }

    public Set<MinBranchDTO> getWorkingBranches() {
        return this.workingBranches;
    }

    public void setWorkingBranches(Set<MinBranchDTO> workingBranches) {
        this.workingBranches = workingBranches;
    }

}
