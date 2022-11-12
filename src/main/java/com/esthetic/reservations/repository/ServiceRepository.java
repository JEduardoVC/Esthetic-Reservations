package com.esthetic.reservations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Branch;
import com.esthetic.reservations.model.Service;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Integer> {
	@Query(value = "select * from service where id_branch = :id", nativeQuery = true)
	List<Service> findAllById_branch(int id);
}