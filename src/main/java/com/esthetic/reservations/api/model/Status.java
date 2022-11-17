package com.esthetic.reservations.api.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Status extends BaseModel<Status> {

	private String status_name;

	public Status() {
		super();
	}

	public Status(Long id, String status_name) {
		super(id);
		this.status_name = status_name;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

	@Override
	public void copy(Status status) {
		this.status_name = status.status_name;
	}

}