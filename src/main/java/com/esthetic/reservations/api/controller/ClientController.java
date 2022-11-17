package com.esthetic.reservations.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.model.Service;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.repository.BranchRepository;
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
	BranchRepository branchRepository;
	
	@Autowired
	ServiceRepository serviceRepository;
	
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
			jsonService.append("id_service", services.getId());
			jsonService.append("id_branch", services.getId_branch().getId());
			jsonService.append("service_name", services.getService_name());
			jsonService.append("duration", services.getDuration());
			jsonService.append("price", services.getPrice());
			json.append("servicio", jsonService);
		}
		return new ResponseEntity<String>(String.valueOf(json), HttpStatus.OK);
	}
	
}
