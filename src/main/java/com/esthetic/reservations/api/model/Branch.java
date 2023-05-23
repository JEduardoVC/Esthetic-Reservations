package com.esthetic.reservations.api.model;

import java.sql.Time;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "branch")
public class Branch extends BaseModel<Branch> {

	@Column(name = "name", length = 30)
	private String name;

	@Column(name = "location", length = 255)
	private String location;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "id_owner", referencedColumnName = "id")
	private UserEntity owner;

	@Column(name = "schedule_open")
	private Time scheduleOpen;

	@Column(name = "schedule_close")
	private Time scheduleClose;

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Employee.class, cascade = { CascadeType.REMOVE,
			CascadeType.DETACH })
	@JoinTable(name = "branch_employee", joinColumns = @JoinColumn(name = "id_branch", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_employee", referencedColumnName = "id"))
	@JsonBackReference
	private Set<Employee> employees = new HashSet<>();

	@PreRemove
	public void removeEmployees() {
		this.getEmployees().forEach(employee -> {
			employee.getWorkingBranches().removeIf(e -> e.equals(this));
		});

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Branch))
			return false;
		Branch branch = (Branch) o;
		return this.getId() != null && this.getId().equals(branch.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(Branch.class);
	}

	public Branch() {
		super();
	}

	public Branch(Long id, String name, String location, Double latitude, Double longitude, UserEntity owner, Time scheduleOpen, Time scheduleClose, Set<Employee> employees) {
		super(id);
		this.name = name;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.owner = owner;
		this.scheduleOpen = scheduleOpen;
		this.scheduleClose = scheduleClose;
		this.employees = employees;
	}

	public Branch(String name, String location, Double latitude, Double longitude, UserEntity owner, Time scheduleOpen, Time scheduleClose, Set<Employee> employees) {
		this.name = name;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
		this.owner = owner;
		this.scheduleOpen = scheduleOpen;
		this.scheduleClose = scheduleClose;
		this.employees = employees;
	}

	
	public Set<Employee> getEmployees() {
		return this.employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
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

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public void copy(Branch branch) {
		this.name = branch.name;
		this.location = branch.location;
		this.latitude = branch.latitude;
		this.longitude = branch.longitude;
		this.owner = branch.owner;
		this.scheduleOpen = branch.scheduleOpen;
		this.scheduleClose = branch.scheduleClose;
		this.employees = branch.employees;
	}

}