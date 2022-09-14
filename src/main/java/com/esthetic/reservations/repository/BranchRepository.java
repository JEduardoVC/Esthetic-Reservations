package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Branch;

@Repository
public interface BranchRepository extends CrudRepository<Branch, Integer> {
	@Query(value = "select * from branch where :columna = :valor", nativeQuery = true)
	Iterable<Branch> where(String columna, String valor);
}

