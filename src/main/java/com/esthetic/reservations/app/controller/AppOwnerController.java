package com.esthetic.reservations.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/app/owner")
public class AppOwnerController {
	
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
		return new ModelAndView("owner/services/formulario-servicio");
	}
	
	@GetMapping("/inventario/agregar")
	public ModelAndView viewCreateInventory() {
		return new ModelAndView("owner/inventory/formulario");
	}
}