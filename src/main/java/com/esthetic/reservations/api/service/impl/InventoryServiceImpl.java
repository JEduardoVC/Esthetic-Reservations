package com.esthetic.reservations.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.InventoryDTO;
import com.esthetic.reservations.api.model.Inventory;
import com.esthetic.reservations.api.repository.InventoryRepository;
import com.esthetic.reservations.api.service.InventoryService;

@Service
public class InventoryServiceImpl extends GenericServiceImpl<Inventory, InventoryDTO> implements InventoryService {

	private BranchServiceImpl branchServiceImpl;
	private InventoryRepository inventoryRepository;
	
	@Autowired
	public InventoryServiceImpl(InventoryRepository repository, ModelMapper model, BranchServiceImpl branchServiceImpl) {
		super(repository, model, Inventory.class, InventoryDTO.class);
		this.branchServiceImpl = branchServiceImpl;
		this.inventoryRepository = repository;
	}
}
