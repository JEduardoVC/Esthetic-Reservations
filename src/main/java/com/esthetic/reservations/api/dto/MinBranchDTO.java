package com.esthetic.reservations.api.dto;

import java.sql.Time;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MinBranchDTO extends GenericModelDTO {

    @NotNull
    @NotEmpty
    private String branchName;

    private String location;

    @NotNull
    private Long ownerId;

    @NotNull
    private Time scheduleOpen;

    @NotNull
    private Time scheduleClose;

    private String state;

    private String municipality;

    public MinBranchDTO() {
    }

    public MinBranchDTO(String branchName, String location, Long ownerId, Time scheduleOpen, Time scheduleClose,
            String state, String municipality) {
        this.branchName = branchName;
        this.location = location;
        this.ownerId = ownerId;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.state = state;
        this.municipality = municipality;
    }

    public MinBranchDTO(Long id, String branchName, String location, Long ownerId, Time scheduleOpen,
            Time scheduleClose, String state, String municipality) {
        super(id);
        this.branchName = branchName;
        this.location = location;
        this.ownerId = ownerId;
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

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Time getScheduleOpen() {
        return this.scheduleOpen;
    }

    public void setScheduleOpen(Time scheduleOpen) {
        this.scheduleOpen = scheduleOpen;
    }

    public Time getScheduleClose() {
        return this.scheduleClose;
    }

    public void setScheduleClose(Time scheduleClose) {
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
