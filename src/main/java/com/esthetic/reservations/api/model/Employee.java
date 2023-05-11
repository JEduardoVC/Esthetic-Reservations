package com.esthetic.reservations.api.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "employee")
public class Employee extends BaseModel<Employee> {

	@OneToOne(targetEntity = UserEntity.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "id_user", referencedColumnName = "id")
	private UserEntity user;

	@ManyToMany(mappedBy = "employees")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonBackReference
    private Set<Branch> workingBranches;

	@OneToMany(mappedBy = "employee", targetEntity = Comment.class)
	private Set<Comment> reviews;

	public UserEntity getUser() {
		return this.user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Set<Branch> getWorkingBranches() {
		return this.workingBranches;
	}

	public void setWorkingBranches(Set<Branch> workingBranches) {
		this.workingBranches = workingBranches;
	}

	@Override
	public void copy(Employee object) {
		this.user = object.user;
		this.workingBranches = object.workingBranches;
	}

}
