package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Status;

@Repository
public interface StatusRepository extends CrudRepository<Status, Integer> {
	@Query(value = "select * from status where :columna = :valor", nativeQuery = true)
	Iterable<Status> where(String columna, String valor);
}

