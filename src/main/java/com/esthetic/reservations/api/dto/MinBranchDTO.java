package com.esthetic.reservations.api.dto;

import java.sql.Time;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class MinBranchDTO extends GenericModelDTO {

    @NotBlank(message = "El nombre de la sucursal es requerida.")
    private String branchName;

    private String location;

    @NotNull(message = "Se requiere el ID del dueño.")
    private Long ownerId;

    @NotNull(message = "La hora de apertura es requerida.")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Time scheduleOpen;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Time scheduleClose;

    @NotNull(message = "La latitud de la sucursal es requerida.")
    private Double latitude;

    @NotNull(message = "La longitud de la sucursal es requerida.")
    private Double longitude;

    private Set<Long> employeesIds;

    public MinBranchDTO() {
    }

    public MinBranchDTO(Long id, String branchName, String location, Long ownerId, Time scheduleOpen,
            Time scheduleClose, Double latitude, Double longitude, Set<Long> employeesIds) {
        super(id);
        this.branchName = branchName;
        this.location = location;
        this.ownerId = ownerId;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.latitude = latitude;
        this.longitude = longitude;
        this.employeesIds = employeesIds;
    }

    public MinBranchDTO(String branchName, String location, Long ownerId, Time scheduleOpen, Time scheduleClose,
            Double latitude, Double longitude, Set<Long> employeesIds) {
        this.branchName = branchName;
        this.location = location;
        this.ownerId = ownerId;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.latitude = latitude;
        this.longitude = longitude;
        this.employeesIds = employeesIds;
    }

    public Set<Long> getEmployeesIds() {
        return this.employeesIds;
    }

    public void setEmployeesIds(Set<Long> employeesIds) {
        this.employeesIds = employeesIds;
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
