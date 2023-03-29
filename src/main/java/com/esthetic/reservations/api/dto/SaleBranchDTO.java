package com.esthetic.reservations.api.dto;

import java.sql.Time;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class SaleBranchDTO extends GenericModelDTO {

    private String branchName;

    private String location;
    
    private MinUserEntityDTO owner;

    private Time scheduleOpen;

    private Time scheduleClose;

    private String state;

    private String municipality;

    /**
     * 
     */
    public SaleBranchDTO() {
        super();
    }

    /**
     * @param branchName
     * @param location
     * @param owner
     * @param scheduleOpen
     * @param scheduleClose
     * @param state
     * @param municipality
     */
    public SaleBranchDTO(String branchName, String location, MinUserEntityDTO owner, Time scheduleOpen,
            Time scheduleClose, String state, String municipality) {
        super();
        this.branchName = branchName;
        this.location = location;
        this.owner = owner;
        this.scheduleOpen = scheduleOpen;
        this.scheduleClose = scheduleClose;
        this.state = state;
        this.municipality = municipality;
    }
    
    /**
     * @param branchName
     * @param location
     * @param owner
     * @param scheduleOpen
     * @param scheduleClose
     * @param state
     * @param municipality
     */
    public SaleBranchDTO(Long id, String branchName, String location, MinUserEntityDTO owner, Time scheduleOpen,
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

    /**
     * @return the branchName
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param branchName the branchName to set
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the owner
     */
    public MinUserEntityDTO getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(MinUserEntityDTO owner) {
        this.owner = owner;
    }

    /**
     * @return the scheduleOpen
     */
    public Time getScheduleOpen() {
        return scheduleOpen;
    }

    /**
     * @param scheduleOpen the scheduleOpen to set
     */
    public void setScheduleOpen(Time scheduleOpen) {
        this.scheduleOpen = scheduleOpen;
    }

    /**
     * @return the scheduleClose
     */
    public Time getScheduleClose() {
        return scheduleClose;
    }

    /**
     * @param scheduleClose the scheduleClose to set
     */
    public void setScheduleClose(Time scheduleClose) {
        this.scheduleClose = scheduleClose;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the municipality
     */
    public String getMunicipality() {
        return municipality;
    }

    /**
     * @param municipality the municipality to set
     */
    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

}
