package com.esthetic.reservations.api.dto;

import java.sql.Time;
import java.util.Set;

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

    private Double latitude;

    private Double longitude;

    private Set<EmployeeDTO> employees;

    public BranchDTO() {
        super();
    }

    public BranchDTO(Long id, String branchName, String location, UserEntity owner, Time scheduleOpen,
            Time scheduleClose, Double latitude, Double longitude, Set<EmployeeDTO> employees) {
        super(id);
        this.branchName = branchName;
        this.location = location;
        this.owner = owner;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.latitude = latitude;
        this.longitude = longitude;
        this.employees = employees;
    }

    public BranchDTO(String branchName, String location, UserEntity owner, Time scheduleOpen, Time scheduleClose,
            Double latitude, Double longitude, Set<EmployeeDTO> employees) {
        this.branchName = branchName;
        this.location = location;
        this.owner = owner;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.latitude = latitude;
        this.longitude = longitude;
        this.employees = employees;
    }

    public Set<EmployeeDTO> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<EmployeeDTO> employees) {
        this.employees = employees;
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

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}