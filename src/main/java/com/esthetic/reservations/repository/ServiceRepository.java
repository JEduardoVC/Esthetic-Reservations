package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Service;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Integer> {
	@Query(value = "select * from service where :columna = :valor", nativeQuery = true)
	Iterable<Service> where(String columna, String valor);
}

