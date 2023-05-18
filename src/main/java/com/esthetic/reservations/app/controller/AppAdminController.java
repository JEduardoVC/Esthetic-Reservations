package com.esthetic.reservations.app.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/app/admin")
public class AppAdminController {
	
	@GetMapping
	public ModelAndView viewAdmin() {
		ModelAndView model = new ModelAndView("admin/usuarios");
		model.addObject("route", "/app/admin/usuarios");
		return model;
	}
	
	@GetMapping("/sucursales")
	public ModelAndView viewSucursales() {
		ModelAndView model = new ModelAndView("admin/sucursales");
		model.addObject("route", "/app/admin/sucursales");
		return model;
	}

	@GetMapping("/usuarios")
	public ModelAndView viewUsuarios() {
		ModelAndView model = new ModelAndView("admin/usuarios");
		model.addObject("route", "/app/admin/usuarios");
		return model;
	}

}
