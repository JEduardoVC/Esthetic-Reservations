package com.esthetic.reservations.api.model;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "branch")
public class Branch extends BaseModel<Branch> {

	@Column(name = "name", length = 30)
	private String name;

	@Column(name = "location", length = 200)
	private String location;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "id_owner", referencedColumnName = "id")
	private UserEntity owner;

	@Column(name = "schedule_open")
	private Time scheduleOpen;

	@Column(name = "schedule_close")
	private Time scheduleClose;

	@Column(name = "state", length = 20)
	private String state;

	@Column(name = "municipality", length = 20)
	private String municipality;

	public Branch() {
		super();
	}

	public Branch(String name, String location, UserEntity owner, Time scheduleOpen, Time scheduleClose, String state,
			String municipality) {
		this.name = name;
		this.location = location;
		this.owner = owner;
		this.scheduleOpen = scheduleOpen;
		this.scheduleClose = scheduleClose;
		this.state = state;
		this.municipality = municipality;
	}

	public Branch(Long id, String name, String location, UserEntity owner, Time scheduleOpen, Time scheduleClose,
			String state, String municipality) {
		super(id);
		this.name = name;
		this.location = location;
		this.owner = owner;
		this.scheduleOpen = scheduleOpen;
		this.scheduleClose = scheduleClose;
		this.state = state;
		this.municipality = municipality;
	}

	public String getBranchName() {
		return name;
	}

	public void setBranchName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public UserEntity getOwner() {
		return this.owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Time getScheduleOpen() {
		return this.scheduleOpen;
	}

	public void setScheduleOpen(Time scheduleOpen) {
		this.scheduleOpen = scheduleOpen;
	}

	public Time getScheduleClose() {
		return this.scheduleClose;
	}

	public void setScheduleClose(Time scheduleClose) {
		this.scheduleClose = scheduleClose;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMunicipality() {
		return this.municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	@Override
	public void copy(Branch branch) {
		this.name = branch.name;
		this.location = branch.location;
		this.state = branch.state;
		this.municipality = branch.municipality;
		this.owner = branch.owner;
		this.scheduleOpen = branch.scheduleOpen;
		this.scheduleClose = branch.scheduleClose;
	}

}