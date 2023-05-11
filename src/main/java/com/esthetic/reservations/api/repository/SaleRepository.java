package com.esthetic.reservations.api.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.Sale;

@Transactional
@Component("SaleRepository")
public interface SaleRepository extends GenericRepository<Sale, Long> {
	
	@Query(value = "select * from sale where id_client = :id and id_branch = :id_branch", nativeQuery = true)
	ArrayList<Sale> findByIdAndByBranch(@Param("id") String id,@Param("id_branch") String id_branch);

    public boolean existsById(Long id);

}