package com.esthetic.reservations.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.repository.ServiceRepository;

@RestController
@RequestMapping("/api/client")
public class ClientController {
	
	@Autowired
	ServiceRepository serviceRep;
	
	@PostMapping("/obtener/sevicios")
	public List<Service> obtenerServicios(@RequestParam("id_branch") int id_branch) {
		return serviceRep.findAllById_branch(id_branch);
	}	
}