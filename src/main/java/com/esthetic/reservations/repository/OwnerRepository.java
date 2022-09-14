package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Owner;

@Repository
public interface OwnerRepository extends CrudRepository<Owner, Integer> {
	@Query(value = "select * from owner where :columna = :valor", nativeQuery = true)
	Iterable<Owner> where(String columna, String valor);
}

