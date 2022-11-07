package com.esthetic.reservations.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "service")
public class Service {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id_service;
	private String service_name;
	private Integer duration;
	private Integer price;
	
	@JoinColumn(name = "id_branch")
	@ManyToOne
	private Branch id_branch;
	
	public Integer getId_service() {
		return id_service;
	}
	public void setId_service(Integer id_service) {
		this.id_service = id_service;
	}
	public String getService_name() {
		return service_name;
	}
	public void setService_name(String service_name) {
		this.service_name = service_name;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Branch getId_branch() {
		return id_branch;
	}
	public void setId_branch(Branch id_branch) {
		this.id_branch = id_branch;
	}
}
