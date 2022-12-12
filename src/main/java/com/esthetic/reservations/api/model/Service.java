package com.esthetic.reservations.api.model;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "service")
public class Service extends BaseModel<Service> {

	private String service_name;

	private Time duration;

	private Double price;

	@JoinColumn(name = "id_branch")
	@ManyToOne
	private Branch idBranch;

	public Service() {
		super();
	}

	public Service(Long id, String service_name, Time duration, Double price, Branch id_branch) {
		super(id);
		this.service_name = service_name;
		this.duration = duration;
		this.price = price;
		this.idBranch = id_branch;
	}
	
	public Service(String service_name, Time duration, Double price, Branch id_branch) {
		this.service_name = service_name;
		this.duration = duration;
		this.price = price;
		this.idBranch = id_branch;
	}

	public String getService_name() {
		return this.service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public Time getDuration() {
		return this.duration;
	}

	public void setDuration(Time duration) {
		this.duration = duration;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Branch getId_branch() {
		return this.idBranch;
	}

	public void setId_branch(Branch id_branch) {
		this.idBranch = id_branch;
	}

	@Override
	public void copy(Service service) {
		this.service_name = service.service_name;
		this.duration = service.duration;
		this.price = service.price;
		this.idBranch = service.idBranch;
	}

}