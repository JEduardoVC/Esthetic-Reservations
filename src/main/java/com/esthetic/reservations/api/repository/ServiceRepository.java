package com.esthetic.reservations.api.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.dto.MinService;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.ServiceDTO;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Service;

@Repository
@Transactional
public interface ServiceRepository extends GenericRepository<Service, Long> {
	
	ArrayList<Service> findAllByIdBranch(Branch idBranch);
}
