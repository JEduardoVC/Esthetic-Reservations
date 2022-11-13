package com.esthetic.reservations.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role extends BaseModel<Role> {

	@Column(name = "name", length = 30, nullable = false)
	private String name;

	public Role() {
		super();
	}

	public Role(Long id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void copy(Role role) {
		this.name = role.name;
	}

}
