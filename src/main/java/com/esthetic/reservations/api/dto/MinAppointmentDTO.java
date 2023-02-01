package com.esthetic.reservations.api.dto;

import java.util.Date;
import java.util.List;

public class MinAppointmentDTO extends GenericModelDTO{

    private Date appointment_Date;

    private Long id_client;

    private Long id_employee;

    private List<Long> id_service;

    private Long id_status;
    
    private Long id_branch;

    public MinAppointmentDTO() {
    }


    public MinAppointmentDTO(Long id, Date appointment_Date, Long id_client, Long id_employee, List<Long> id_service, Long id_status, Long id_branch) {
        super(id);
        this.appointment_Date = appointment_Date;
        this.id_client = id_client;
        this.id_employee = id_employee;
        this.id_service = id_service;
        this.id_status = id_status;
        this.id_branch = id_branch;
    }

    public Date getAppointment_Date() {
        return this.appointment_Date;
    }

    public void setAppointment_Date(Date appointment_Date) {
        this.appointment_Date = appointment_Date;
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

    public List<Long> getId_service() {
        return this.id_service;
    }

    public void setId_service(List<Long> id_service) {
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
}