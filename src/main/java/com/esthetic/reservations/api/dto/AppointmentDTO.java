package com.esthetic.reservations.api.dto;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.model.Status;
import com.esthetic.reservations.api.model.UserEntity;

public class AppointmentDTO extends GenericModelDTO{

    private Date date_created;

    private Date appointment_date;
    
    private Time appointmnet_time;

    private UserEntity id_client;

    private UserEntity id_employee;

    private List<Service> id_service;

    private Status id_status;
    
    private Branch id_branch;

    public AppointmentDTO() {
    }


    public AppointmentDTO(Long id, Date date_created, Date appointment_date, Time appointment_time, UserEntity id_client, UserEntity id_employee, List<Service> id_service, Status id_status, Branch id_branch) {
        super(id);
        this.date_created = date_created;
        this.appointment_date = appointment_date;
        this.appointmnet_time = appointment_time;
        this.id_client = id_client;
        this.id_employee = id_employee;
        this.id_service = id_service;
        this.id_status = id_status;
        this.id_branch = id_branch;
    }
    
    public AppointmentDTO(Date date_created, Date appointment_date, Time appointment_time, UserEntity id_client, UserEntity id_employee, List<Service> id_service, Status id_status, Branch id_branch) {
        super();
        this.date_created = date_created;
        this.appointment_date = appointment_date;
        this.appointmnet_time = appointment_time;
        this.id_client = id_client;
        this.id_employee = id_employee;
        this.id_service = id_service;
        this.id_status = id_status;
        this.id_branch = id_branch;
    }


    public Date getDate_created() {
        return this.date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getAppointment_date() {
        return this.appointment_date;
    }

    public void setAppointment_date(Date appointment_date) {
        this.appointment_date = appointment_date;
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

    public List<Service> getId_service() {
        return this.id_service;
    }

    public void setId_service(List<Service> id_service) {
        this.id_service = id_service;
    }

    public Status getId_status() {
        return this.id_status;
    }

    public void setId_status(Status id_status) {
        this.id_status = id_status;
    }

	public Branch getId_branch() {
		return id_branch;
	}

	public void setId_branch(Branch id_branch) {
		this.id_branch = id_branch;
	}
	
	public Time getAppointmnet_time() {
		return appointmnet_time;
	}


	public void setAppointmnet_time(Time appointmnet_time) {
		this.appointmnet_time = appointmnet_time;
	}
}