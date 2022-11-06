package com.esthetic.reservations.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "employee")

public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id_employee;
	private String first_name;
	private String middle_name;
	private String last_name;
	private String phone_number;
	private String addres;
	
	@JoinColumn(name = "id_branch")
	@ManyToOne
	private Branch id_branch;
	
	@JoinColumn(name = "id_owner")
	@ManyToOne
	private Owner id_owner;
	
	public Integer getId_employee() {
		return id_employee;
	}
	public void setId_employee(Integer id_employee) {
		this.id_employee = id_employee;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getMiddle_name() {
		return middle_name;
	}
	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getAddres() {
		return addres;
	}
	public void setAddres(String addres) {
		this.addres = addres;
	}
	public Branch getId_branch() {
		return id_branch;
	}
	public void setId_branch(Branch id_branch) {
		this.id_branch = id_branch;
	}
	public Owner getId_owner() {
		return id_owner;
	}
	public void setId_owner(Owner id_owner) {
		this.id_owner = id_owner;
	}
	
}
