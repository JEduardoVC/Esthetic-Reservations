package com.esthetic.reservations.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_role", nullable = false)
	private Long roleId;

	@Column(name = "name", length = 30, nullable = false)
	private String name;

	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	public Role(Long roleId, String name) {
		this.roleId = roleId;
		this.name = name;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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

	@Override
	public String toString() {
		return "{" +
				" roleId='" + getRoleId() + "'" +
				", name='" + getName() + "'" +
				"}";
	}

}
