package com.esthetic.reservations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {
	Client findByEmail(String email);
}

