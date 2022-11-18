package com.esthetic.reservations.api.dto;

import java.util.Date;

import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.model.Status;
import com.esthetic.reservations.api.model.UserEntity;

public class AppointmentDTO extends GenericModelDTO{

    private Date date_created;

    private Date appointment_Date;

    private UserEntity id_client;

    private UserEntity id_employee;

    private Service id_service;

    private Status id_status;

    public AppointmentDTO() {
    }


    public AppointmentDTO(Long id, Date date_created, Date appointment_Date, UserEntity id_client, UserEntity id_employee, Service id_service, Status id_status) {
        super(id);
        this.date_created = date_created;
        this.appointment_Date = appointment_Date;
        this.id_client = id_client;
        this.id_employee = id_employee;
        this.id_service = id_service;
        this.id_status = id_status;
    }


    public Date getDate_created() {
        return this.date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getAppointment_Date() {
        return this.appointment_Date;
    }

    public void setAppointment_Date(Date appointment_Date) {
        this.appointment_Date = appointment_Date;
    }

    public UserEntity getId_client() {
        return this.id_client;
    }

    public void setId_client(UserEntity id_client) {
        this.id_client = id_client;
    }

    public UserEntity getId_employee() {
        return this.id_employee;
    }

    public void setId_employee(UserEntity id_employee) {
        this.id_employee = id_employee;
    }

    public Service getId_service() {
        return this.id_service;
    }

    public void setId_service(Service id_service) {
        this.id_service = id_service;
    }

    public Status getId_status() {
        return this.id_status;
    }

    public void setId_status(Status id_status) {
        this.id_status = id_status;
    }

}