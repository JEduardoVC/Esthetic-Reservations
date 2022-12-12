package com.esthetic.reservations.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/app/admin")
public class AppAdminController {

	@GetMapping
	public ModelAndView viewAdmin() {
		return new ModelAndView("admin/user/usuarios");
	}

	@GetMapping("/sucursales")
	public ModelAndView viewSucursales() {
		return new ModelAndView("admin/branch/sucursales");
	}

	@GetMapping("/usuarios")
	public ModelAndView viewUsuarios() {
		return new ModelAndView("admin/user/usuarios");
	}

}