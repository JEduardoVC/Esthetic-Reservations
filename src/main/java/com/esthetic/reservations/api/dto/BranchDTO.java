package com.esthetic.reservations.api.dto;

import java.security.Timestamp;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.esthetic.reservations.api.model.UserEntity;

public class BranchDTO extends GenericModelDTO {

    @NotNull
    @NotEmpty
    private String branchName;

    private String location;

    @NotNull
    private UserEntity owner;

    @NotNull
    private Timestamp scheduleOpen;

    @NotNull
    private Timestamp scheduleClose;

    private String state;

    private String municipality;

    public BranchDTO() {
        super();
    }

    public BranchDTO(String branchName, String location, UserEntity owner, Timestamp scheduleOpen,
            Timestamp scheduleClose, String state, String municipality) {
        this.branchName = branchName;
        this.location = location;
        this.owner = owner;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.state = state;
        this.municipality = municipality;
    }

    public BranchDTO(Long id, String branchName, String location, UserEntity owner, Timestamp scheduleOpen,
            Timestamp scheduleClose, String state, String municipality) {
        super(id);
        this.branchName = branchName;
        this.location = location;
        this.owner = owner;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.state = state;
        this.municipality = municipality;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    public Timestamp getScheduleOpen() {
        return this.scheduleOpen;
    }

    public void setScheduleOpen(Timestamp scheduleOpen) {
        this.scheduleOpen = scheduleOpen;
    }

    public Timestamp getScheduleClose() {
        return this.scheduleClose;
    }

    public void setScheduleClose(Timestamp scheduleClose) {
        this.scheduleClose = scheduleClose;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMunicipality() {
        return this.municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

}