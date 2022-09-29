package com.esthetic.reservations.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id_appointment;
	private Date date_created;
	private Date appointment_Date;

	@ManyToOne
	private Client id_client;

	@ManyToOne
	private Employee id_employee;

	@ManyToOne
	private Service id_service;

	@ManyToOne
	private Status id_status;

	public Integer getId_appointment() {
		return id_appointment;
	}

	public void setId_appointment(Integer id_appointment) {
		this.id_appointment = id_appointment;
	}

	public Date getDate_created() {
		return date_created;
	}

	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}

	public Date getAppointment_Date() {
		return appointment_Date;
	}

	public void setAppointment_Date(Date appointment_Date) {
		this.appointment_Date = appointment_Date;
	}

	public Client getId_client() {
		return id_client;
	}

	public void setId_client(Client id_client) {
		this.id_client = id_client;
	}

	public Employee getId_employee() {
		return id_employee;
	}

	public void setId_employee(Employee id_employee) {
		this.id_employee = id_employee;
	}

	public Service getId_service() {
		return id_service;
	}

	public void setId_service(Service id_service) {
		this.id_service = id_service;
	}

	public Status getId_status() {
		return id_status;
	}

	public void setId_status(Status id_status) {
		this.id_status = id_status;
	}
}
