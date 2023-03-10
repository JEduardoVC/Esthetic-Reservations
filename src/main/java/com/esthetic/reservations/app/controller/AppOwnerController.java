package com.esthetic.reservations.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.api.repository.BranchRepository;
import com.esthetic.reservations.api.repository.InventoryRepository;
import com.esthetic.reservations.api.repository.UserRepository;

@RestController
@RequestMapping("/app/owner")
public class AppOwnerController {
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	BranchRepository branchRepository;
	
	@Autowired
	UserRepository ownerRepository;
	
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
		return new ModelAndView("owner/inventory/inventario");
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
	
	@GetMapping("/inventario/actualizar")
	public ModelAndView viewUpdateInventory() {
		return new ModelAndView("owner/inventory/agregar");
	}
	
	@GetMapping("/validar/citas")
	public ModelAndView viewValidateAppointment() {
		return new ModelAndView("owner/appointment/validar_cita");
	}
}