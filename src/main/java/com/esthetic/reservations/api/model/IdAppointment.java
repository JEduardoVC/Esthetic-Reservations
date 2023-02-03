package com.esthetic.reservations.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GeneratorType;

@Entity
@Table(name = "id_appointments")
public class IdAppointment extends BaseModel<IdAppointment> {

	@Override
	public void copy(IdAppointment object) {		
	}
}
