package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Appointment;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Integer> {
}
