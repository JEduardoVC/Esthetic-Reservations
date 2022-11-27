package com.esthetic.reservations.api.repository;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Inventory;
import com.esthetic.reservations.api.model.Service;

@Repository
@Transactional
@Component("InventoryRepository")
public interface InventoryRepository extends GenericRepository<Inventory, Long> {
	ArrayList<Inventory> findAllByIdBranch(Branch branch);
}