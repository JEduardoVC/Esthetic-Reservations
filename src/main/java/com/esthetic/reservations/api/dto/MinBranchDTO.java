package com.esthetic.reservations.api.dto;

import java.sql.Time;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MinBranchDTO extends GenericModelDTO {

    @NotNull
    @NotEmpty
    private String branch_name;

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

    public MinBranchDTO(String branch_name, String location, Long ownerId, Time scheduleOpen, Time scheduleClose,
            String state, String municipality) {
        this.branch_name = branch_name;
        this.location = location;
        this.ownerId = ownerId;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.state = state;
        this.municipality = municipality;
    }

    public MinBranchDTO(Long id, String branch_name, String location, Long ownerId, Time scheduleOpen,
            Time scheduleClose, String state, String municipality) {
        super(id);
        this.branch_name = branch_name;
        this.location = location;
        this.ownerId = ownerId;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.state = state;
        this.municipality = municipality;
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

    public String getBranch_name() {
        return this.branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
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
