package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Schedule;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Integer> {
	@Query(value = "select * from schedule where :columna = :valor", nativeQuery = true)
	Iterable<Schedule> where(String columna, String valor);
}

