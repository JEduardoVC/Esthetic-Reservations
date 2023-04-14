package com.esthetic.reservations.api.dto;

import java.sql.Time;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.esthetic.reservations.api.model.UserEntity;

public class BranchDTO extends GenericModelDTO {

    @NotBlank(message = "El nombre de la sucursal es requerido.")
    private String branchName;

    private String location;

    @NotNull(message = "El dueño es requerido.")
    @NotEmpty(message = "El dueño es requerido.")
    private UserEntity owner;

    @NotNull(message = "La hora de apertura es requerida.")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Time scheduleOpen;

    @NotNull(message = "La hora de cierre es requerida.")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Time scheduleClose;

    private String state;

    private String municipality;

    public BranchDTO() {
        super();
    }

    public BranchDTO(String branchName, String location, UserEntity owner, Time scheduleOpen,
            Time scheduleClose, String state, String municipality) {
        this.branchName = branchName;
        this.location = location;
        this.owner = owner;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.state = state;
        this.municipality = municipality;
    }

    public BranchDTO(Long id, String branchName, String location, UserEntity owner, Time scheduleOpen,
            Time scheduleClose, String state, String municipality) {
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