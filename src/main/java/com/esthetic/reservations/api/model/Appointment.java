package com.esthetic.reservations.api.model;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

@Entity
@Table(name = "appointment")
public class Appointment extends BaseModel<Appointment> {

	private Date date_created;

	private Date appointment_Date;
	
	private Time appointmnet_time;
	
	@ManyToOne
	private UserEntity id_client;

	@ManyToOne
	private UserEntity id_employee;
	
	@ManyToOne
	private Service id_service;

	@ManyToOne
	private Status id_status;
	
	@ManyToOne
	private Branch idBranch;
	
	@ManyToMany(targetEntity = IdAppointment.class)
	@JoinTable(name = "appointment_services", joinColumns = @JoinColumn(name = "id_appointment_service", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_appointments", referencedColumnName = "id"))
	private List<IdAppointment> appointmentServices = new ArrayList<>();

	public Appointment() {
		super();
	}
	
	public Appointment(Date date_created, Date appointment_Date, Time appointment_time, UserEntity id_client, UserEntity id_employee, Service id_service, Status id_status, Branch id_branch) {
		super();
		this.date_created = date_created;
		this.appointment_Date = appointment_Date;
		this.appointmnet_time = appointment_time;
		this.id_client = id_client;
		this.id_employee = id_employee;
		this.id_service = id_service;
		this.id_status = id_status;
		this.idBranch = id_branch;
	}

	public Appointment(Long id, Date date_created, Date appointment_Date, Time appointment_time, UserEntity id_client, UserEntity id_employee,
			Service id_service, Status id_status, Branch id_branch, List<IdAppointment> appointmentServices) {
		super(id);
		this.date_created = date_created;
		this.appointment_Date = appointment_Date;
		this.appointmnet_time = appointment_time;
		this.id_client = id_client;
		this.id_employee = id_employee;
		this.id_service = id_service;
		this.id_status = id_status;
		this.idBranch = id_branch;
		this.appointmentServices = appointmentServices;
	}
	
	public Appointment(Long id, Date date_created, Date appointment_Date, Time appointment_time, UserEntity id_client, UserEntity id_employee,
			Service id_service, Status id_status, Branch id_branch) {
		super(id);
		this.date_created = date_created;
		this.appointment_Date = appointment_Date;
		this.appointmnet_time = appointment_time;
		this.id_client = id_client;
		this.id_employee = id_employee;
		this.id_service = id_service;
		this.id_status = id_status;
		this.idBranch = id_branch;
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
		return id_service;
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
		return this.idBranch;
	}
	
	public void setId_branch(Branch id_branch) {
		this.idBranch = id_branch;
	}

	public Time getAppointmnet_time() {
		return appointmnet_time;
	}

	public void setAppointmnet_time(Time appointmnet_time) {
		this.appointmnet_time = appointmnet_time;
	}

	public List<IdAppointment> getAppointmentServices() {
		return appointmentServices;
	}

	public void setAppointmentServices(List<IdAppointment> appointmentServices) {
		this.appointmentServices = appointmentServices;
	}

	@Override
	public void copy(Appointment appointment) {
		this.date_created = appointment.date_created;
		this.appointment_Date = appointment.appointment_Date;
		this.appointmnet_time = appointment.appointmnet_time;
		this.id_client = appointment.id_client;
		this.id_employee = appointment.id_employee;
		this.id_status = appointment.id_status;
		this.idBranch = appointment.idBranch;
	}

}
