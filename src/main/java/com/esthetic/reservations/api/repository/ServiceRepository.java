package com.esthetic.reservations.api.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.Service;

@Repository
@Transactional
@EnableJpaRepositories
public interface ServiceRepository extends GenericRepository<Service, Long> {

	@Query(value = "select * from service where id_branch = :id", nativeQuery = true)
	List<Service> findAllById_branch(int id);
	
	List<Service> findAllByIdBranch(Long idBranch);
}
