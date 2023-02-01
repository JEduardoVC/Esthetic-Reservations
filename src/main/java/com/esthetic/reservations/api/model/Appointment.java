package com.esthetic.reservations.api.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment extends BaseModel<Appointment> {

	private Date date_created;

	private Date appointment_Date;

	@ManyToOne
	private UserEntity id_client;

	@ManyToOne
	private UserEntity id_employee;

	@ManyToOne
	private Service id_service;

	@ManyToOne
	private Status id_status;
	
	@ManyToOne
	private Branch id_branch;

	public Appointment() {
		super();
	}
	
	public Appointment(Date date_created, Date appointment_Date, UserEntity id_client, UserEntity id_employee, Service id_service, Status id_status, Branch id_branch) {
		super();
		this.date_created = date_created;
		this.appointment_Date = appointment_Date;
		this.id_client = id_client;
		this.id_employee = id_employee;
		this.id_service = id_service;
		this.id_status = id_status;
		this.id_branch = id_branch;
	}

	public Appointment(Long id, Date date_created, Date appointment_Date, UserEntity id_client, UserEntity id_employee,
			Service id_service, Status id_status, Branch id_branch) {
		super(id);
		this.date_created = date_created;
		this.appointment_Date = appointment_Date;
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
	
	public Branch getId_branch() {
		return this.id_branch;
	}
	
	public void setId_branch(Branch id_branch) {
		this.id_branch = id_branch;
	}

	@Override
	public void copy(Appointment appointment) {
		this.date_created = appointment.date_created;
		this.appointment_Date = appointment.appointment_Date;
		this.id_client = appointment.id_client;
		this.id_employee = appointment.id_employee;
		this.id_service = appointment.id_service;
		this.id_status = appointment.id_status;
	}

}
