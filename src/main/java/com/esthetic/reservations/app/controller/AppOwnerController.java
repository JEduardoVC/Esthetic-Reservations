package com.esthetic.reservations.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Inventory;
import com.esthetic.reservations.api.repository.BranchRepository;
import com.esthetic.reservations.api.repository.InventoryRepository;
import com.esthetic.reservations.api.service.impl.InventoryServiceImpl;

@RestController
@RequestMapping("/app/owner")
public class AppOwnerController {
	
	private Long branch_id;
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	BranchRepository branchRepository;
	
	@GetMapping()
	public ModelAndView viewOwner() {
		return new ModelAndView("owner/owner");
	}
	
	@GetMapping("/servicios")
	public ModelAndView viewServices() {
		return new ModelAndView("owner/services/servicios");
	}
	
	@GetMapping("/inventario")
	public ModelAndView viewInventory() {
		Branch branch = branchRepository.findById(branch_id).orElse(null);
		List<Inventory> listaInventario = inventoryRepository.findAllByIdBranch(branch);
		return new ModelAndView("owner/inventory/inventario").addObject("invetories", listaInventario);
	}
	
	@GetMapping("/inventario/{id}")
	public ModelAndView viewInventoryId(@PathVariable("id") Long id) {
		branch_id = id;
		return viewInventory();
	}
	
	@GetMapping("/servicios/agregar")
	public ModelAndView viewCreateService() {
		return new ModelAndView("owner/services/agregar");
	}
	
	@GetMapping("/servicios/actualizar")
	public ModelAndView viewUpdateService() {
		return new ModelAndView("owner/services/agregar");
	}
	
	@GetMapping("/inventario/agregar")
	public ModelAndView viewCreateInventory() {
		return new ModelAndView("owner/inventory/agregar");
	}
	
	@GetMapping("/inventario/actualizar/{id}")
	public ModelAndView viewUpdateInventory(@PathVariable("id") Long id) {
		return new ModelAndView("owner/inventory/actualizar").addObject("inventory", inventoryRepository.findById(id).orElse(null));
	}
}