package com.esthetic.reservations.api.repository;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.Inventory;

@Repository
@Transactional
@Component("InventoryRepository")
public interface InventoryRepository extends GenericRepository<Inventory, Long> {

}