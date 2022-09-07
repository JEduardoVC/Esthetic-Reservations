package com.esthetic.reservations.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping
public class Controller {
	
	@GetMapping
	public ModelAndView Index() {
		ModelAndView inicio = new ModelAndView("index");
		return inicio;
	}
}
