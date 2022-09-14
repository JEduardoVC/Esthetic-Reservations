package com.esthetic.reservations.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
	@Query(value = "select * from client where :columna = :valor", nativeQuery = true)
	Iterable<Client> where(String columna, String valor);
}

