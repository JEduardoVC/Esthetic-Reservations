package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericRepository<T> extends CrudRepository<T, Integer> {
	@Query(value = "Select * from :table where :columna = :valor", nativeQuery = true)
	Iterable<T> where(String table, String columna, String valor);
	
}
