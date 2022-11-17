package com.esthetic.reservations.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/app/admin")
public class AppAdminController {

	@GetMapping
	public ModelAndView viewAdmin() {
		return new ModelAndView("admin/admin");
	}

	@GetMapping("/sucursales")
	public ModelAndView viewUsuarios() {
		return new ModelAndView("admin/branch/sucursales");
	}

}