package com.esthetic.reservations.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "schedule")
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id_schedule;
	private Timestamp time_bottom;
	private Timestamp time_top;
	
	@JoinColumn(name = "id_employee")
	@ManyToOne
	private Employee id_employee;
	
	public Integer getId_schedule() {
		return id_schedule;
	}
	public void setId_schedule(Integer id_schedule) {
		this.id_schedule = id_schedule;
	}
	public Timestamp getTime_bottom() {
		return time_bottom;
	}
	public void setTime_bottom(Timestamp time_bottom) {
		this.time_bottom = time_bottom;
	}
	public Timestamp getTime_top() {
		return time_top;
	}
	public void setTime_top(Timestamp time_top) {
		this.time_top = time_top;
	}
	public Employee getId_employee() {
		return id_employee;
	}
	public void setId_employee(Employee id_employee) {
		this.id_employee = id_employee;
	}
	
}
