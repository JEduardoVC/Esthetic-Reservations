package com.esthetic.reservations.api.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.Service;

@Repository
@Transactional
@Component("ServiceRepository")
public interface ServiceRepository extends GenericRepository<Service, Long> {

	@Query(value = "select * from service where id = :id", nativeQuery = true)
	List<Service> findAllById_branch(int id);

}