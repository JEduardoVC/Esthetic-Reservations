package com.esthetic.reservations.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "branch")
public class Branch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id_branch;
	
	private String name;
	private String location;
	
	@JoinColumn(name = "id_owner")
	@ManyToOne
	private Owner id_owner;
	
	public Integer getId_branch() {
		return id_branch;
	}
	public void setId_branch(Integer id_branch) {
		this.id_branch = id_branch;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Owner getId_owner() {
		return id_owner;
	}
	public void setId_owner(Owner id_owner) {
		this.id_owner = id_owner;
	}
	
	
}
