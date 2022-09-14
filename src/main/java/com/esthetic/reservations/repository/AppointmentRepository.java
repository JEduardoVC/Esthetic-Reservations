package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Appointment;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Integer> {
	@Query(value = "select * from appointment where :columna = :valor", nativeQuery = true)
	Iterable<Appointment> where(String columna, String valor);
}
