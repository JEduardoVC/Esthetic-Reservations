package com.esthetic.reservations.api.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.Branch;

@Repository
@Transactional
@Component("BranchRepository")
public interface BranchRepository extends GenericRepository<Branch, Long> {

	public Optional<Branch> findByName(String name);

}