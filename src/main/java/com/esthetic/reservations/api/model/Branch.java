package com.esthetic.reservations.api.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "branch")
public class Branch extends BaseModel<Branch> {

	private String name;

	private String location;

	@JoinColumn(name = "id_owner")
	@ManyToOne
	private UserEntity id_owner;

	public Branch() {
		super();
	}

	public Branch(Long id, String name, String location, UserEntity id_owner) {
		super(id);
		this.name = name;
		this.location = location;
		this.id_owner = id_owner;
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

	public UserEntity getId_owner() {
		return this.id_owner;
	}

	public void setId_owner(UserEntity id_owner) {
		this.id_owner = id_owner;
	}

	@Override
	public void copy(Branch branch) {
		this.name = branch.name;
		this.location = branch.location;
		this.id_owner = branch.id_owner;
	}

}
