package com.esthetic.reservations.api.dto;

import java.sql.Timestamp;

import com.esthetic.reservations.api.model.UserEntity;

public class ScheduleDTO extends GenericModelDTO{

    private Timestamp time_bottom;

    private Timestamp time_top;

    private UserEntity id_employee;

    public ScheduleDTO() {
    }


    public ScheduleDTO(Long id, Timestamp time_bottom, Timestamp time_top, UserEntity id_employee) {
        super(id);
        this.time_bottom = time_bottom;
        this.time_top = time_top;
        this.id_employee = id_employee;
    }
    

    public Timestamp getTime_bottom() {
        return this.time_bottom;
    }

    public void setTime_bottom(Timestamp time_bottom) {
        this.time_bottom = time_bottom;
    }

    public Timestamp getTime_top() {
        return this.time_top;
    }

    public void setTime_top(Timestamp time_top) {
        this.time_top = time_top;
    }

    public UserEntity getId_employee() {
        return this.id_employee;
    }

    public void setId_employee(UserEntity id_employee) {
        this.id_employee = id_employee;
    }

}
