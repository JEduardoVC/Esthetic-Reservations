package com.esthetic.reservations.api.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "schedule")
public class Schedule extends BaseModel<Schedule> {

	private Timestamp time_bottom;

	private Timestamp time_top;

	@JoinColumn(name = "id_employee")
	@ManyToOne
	private UserEntity id_employee;

	public Schedule() {
		super();
	}

	public Schedule(Long id, Timestamp time_bottom, Timestamp time_top, UserEntity id_employee) {
		super(id);
		this.time_bottom = time_bottom;
		this.time_top = time_top;
		this.id_employee = id_employee;
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

	public UserEntity getId_employee() {
		return this.id_employee;
	}

	public void setId_employee(UserEntity id_employee) {
		this.id_employee = id_employee;
	}

	@Override
	public void copy(Schedule schedule) {
		this.time_bottom = schedule.time_bottom;
		this.time_top = schedule.time_top;
		this.id_employee = schedule.id_employee;
	}

}
