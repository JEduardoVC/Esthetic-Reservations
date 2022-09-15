package com.esthetic.reservations.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.model.Client;
import com.esthetic.reservations.repository.ClientRepository;

@RestController
@RequestMapping
public class Controller {
	
	private Client clienteLogueado;
	
	@Autowired
	ClientRepository clienteRepository;
	
	@GetMapping
	public ModelAndView Index() {
		ModelAndView inicio = new ModelAndView("login/login");
		return inicio;
	}
	
	public ModelAndView Index(Map<String, String> alertas) {
		ModelAndView inicio = new ModelAndView("login/login");
		inicio.addObject("alertas", alertas);
		return inicio;
	}
	
	@PostMapping("/")
	public ModelAndView Login(@ModelAttribute Client cliente) {
		Client client = clienteRepository.findByEmail(cliente.getEmail());
		Map<String, String> alertas = new HashMap<String, String>();
		if(client != null) {
			if(client.getPassword().equals(cliente.getPassword())) {
				clienteLogueado = client;
				return new ModelAndView("redirect:Dashboard");
			} else {
				alertas.put("error", "Contraseña incorrecta");
			}
		} else {
			alertas.put("error", "Usuario no existe");
		}
		return Index(alertas);
	}
	
	@GetMapping("/Dashboard")
	public ModelAndView Dashboard() {
		return new ModelAndView("index");
	}
}
