package com.esthetic.reservations.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.model.Branch;

@Repository
public interface BranchRepository extends CrudRepository<Branch, Integer> {
	Branch findByName(String name);
}