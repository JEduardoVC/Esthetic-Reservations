package com.esthetic.reservations.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.service.impl.InventoryServiceImpl;

@RestController
@RequestMapping("/api/owner/inventario")
public class InventoryController {
	@Autowired
	InventoryServiceImpl inventoryServiceImpl;
	
	
}
