package com.esthetic.reservations.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "branch")
public class Branch extends BaseModel<Branch> {

	@Column(name = "name", length = 30)
	private String name;

	@Column(name = "location", length = 200)
	private String location;

	@ManyToOne
	@JoinColumn(name = "id_owner", referencedColumnName = "id")
	private UserEntity owner;

	public Branch() {
		super();
	}

	public Branch(String name, String location, UserEntity owner) {
		this.name = name;
		this.location = location;
		this.owner = owner;
	}

	public Branch(Long id, String name, String location, UserEntity owner) {
		super(id);
		this.name = name;
		this.location = location;
		this.owner = owner;
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

	public UserEntity getOwner() {
		return this.owner;
	}

	public void setOwner(UserEntity owner) {
		this.owner = owner;
	}

	@Override
	public void copy(Branch branch) {
		this.name = branch.name;
		this.location = branch.location;
		this.owner = branch.owner;
	}

}