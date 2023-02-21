package com.esthetic.reservations.api.dto;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MinAppointmentDTO extends GenericModelDTO{

    private String appointment_date;
    
    private Time appointment_time;

    private Long id_client;

    private Long id_employee;

    private ArrayList<Long> id_service;

    private Long id_status;
    
    private Long id_branch;

    public MinAppointmentDTO() {
    }


    public MinAppointmentDTO(Long id, String appointment_date, Time appointment_time, Long id_client, Long id_employee, ArrayList<Long> id_service, Long id_status, Long id_branch) {
        super(id);
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.id_client = id_client;
        this.id_employee = id_employee;
        this.id_service = id_service;
        this.id_status = id_status;
        this.id_branch = id_branch;
    }

    public String getappointment_date() {
        return this.appointment_date;
    }

    public void setappointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public Long getId_client() {
        return this.id_client;
    }

    public void setId_client(Long id_client) {
        this.id_client = id_client;
    }

    public Long getId_employee() {
        return this.id_employee;
    }

    public void setId_employee(Long id_employee) {
        this.id_employee = id_employee;
    }

    public ArrayList<Long> getId_service() {
        return this.id_service;
    }

    public void setId_service(ArrayList<Long> id_service) {
        this.id_service = id_service;
    }

    public Long getId_status() {
        return this.id_status;
    }

    public void setId_status(Long id_status) {
        this.id_status = id_status;
    }

	public Long getId_branch() {
		return id_branch;
	}

	public void setId_branch(Long id_branch) {
		this.id_branch = id_branch;
	}

	public Time getAppointment_time() {
		return appointment_time;
	}

	public void setAppointment_time(Time appointment_time) {
		this.appointment_time = appointment_time;
	}
}