package com.esthetic.reservations.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.esthetic.reservations.model.Branch;
import com.esthetic.reservations.model.Service;
import com.esthetic.reservations.repository.BranchRepository;
import com.esthetic.reservations.repository.OwnerRepository;
import com.esthetic.reservations.repository.ServiceRepository;

@RestController
@RequestMapping
public class ClientController {
	
	@Autowired
	BranchRepository branchRepository;
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@GetMapping("/client")
	public ModelAndView viewClient() {
		return new ModelAndView("client/client");
	}
	
	@PostMapping("/api/surcursal/obtener")
	public ResponseEntity<String> obtenerSucursal() {
		JSONObject json = new JSONObject();
		for (Branch sucursal : branchRepository.findAll()) {
			JSONObject jsonSucursal = new JSONObject();
			jsonSucursal.append("name", sucursal.getName());
			jsonSucursal.append("location", sucursal.getLocation());
			json.append("sucursal", jsonSucursal);
		}
		return new ResponseEntity<String>(String.valueOf(json), HttpStatus.OK);
	}
	
	@PostMapping("/api/servicios/obtener")
	public ResponseEntity<String> obtenerServicios() {
		JSONObject json = new JSONObject();
		for (Service services : serviceRepository.findAllById_branch(1)) {
			JSONObject jsonService = new JSONObject();
			jsonService.append("id_service", services.getId_service());
			jsonService.append("id_branch", services.getId_branch().getId_branch());
			jsonService.append("service_name", services.getService_name());
			jsonService.append("duration", services.getDuration());
			jsonService.append("price", services.getPrice());
			json.append("servicio", jsonService);
		}
		return new ResponseEntity<String>(String.valueOf(json), HttpStatus.OK);
	}
}
