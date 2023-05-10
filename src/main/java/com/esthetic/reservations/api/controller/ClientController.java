package com.esthetic.reservations.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.AppointmentDTO;
import com.esthetic.reservations.api.dto.InventoryDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.SaleDTO;
import com.esthetic.reservations.api.dto.ServiceDTO;
import com.esthetic.reservations.api.service.impl.AppointmentServiceImpl;
import com.esthetic.reservations.api.service.impl.InventoryServiceImpl;
import com.esthetic.reservations.api.service.impl.SaleServiceImpl;
import com.esthetic.reservations.api.service.impl.SeriviceServiceImpl;

@RestController
@RequestMapping("/api/client")
public class ClientController {
	
	@Autowired
	InventoryServiceImpl inventoryServiceImpl;
	
	@Autowired
	SeriviceServiceImpl serviceServiceImpl;
	
	@Autowired
	AppointmentServiceImpl appointmentServiceImpl;
	
	@Autowired
	SaleServiceImpl saleServiceImpl;
	
	@GetMapping("/products/branch/{id}")
	public ResponseDTO<InventoryDTO> obtenerInventarioSucursal(@PathVariable("id") Long id) {
		return inventoryServiceImpl.findAllByBranchId(id);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<InventoryDTO> obtener(@PathVariable("id") Long id) {
		return new ResponseEntity<InventoryDTO>(inventoryServiceImpl.findById(id), HttpStatus.OK);
	}
	
	@GetMapping("/service/branch/{id}")
	public ResponseDTO<ServiceDTO> obtenerServicioBranch(@PathVariable Long id) {
		return serviceServiceImpl.findAllIdBranch(id);
	}
	
	@GetMapping("/service/{id}")
	public ResponseEntity<ServiceDTO> obtenerServicio(@PathVariable Long id) {
		return new ResponseEntity<ServiceDTO>(serviceServiceImpl.findById(id), HttpStatus.OK);
	}
	
	@GetMapping("/appointment/{id}")
	public ResponseEntity<AppointmentDTO> obtenerCita(@PathVariable Long id) {
		return new ResponseEntity<AppointmentDTO>(appointmentServiceImpl.getId(id), HttpStatus.OK);
	}
	
	@GetMapping("/appointment/date/branch/{id}/{date}")
	public ResponseDTO<AppointmentDTO> obtenerCitasFecha(@PathVariable Long id, @PathVariable String date) {
		return appointmentServiceImpl.findAllByDate(id, date);
	}
	
	@GetMapping("/appointment/branch/{id}/{branch}")
	public ResponseDTO<AppointmentDTO> obtenerCitaUsuario(@PathVariable("id") Long id_client, @PathVariable("branch") Long id_branch) {
		return appointmentServiceImpl.findByIdAndbyBranch(id_client, id_branch);
	}
	
	@GetMapping("/sale/{id}")
	public ResponseEntity<SaleDTO> obtenerVenta(@PathVariable Long id) {
		return new ResponseEntity<SaleDTO>(saleServiceImpl.findById(id), HttpStatus.OK);
	}
	
	@GetMapping("/sales/branch/{id}/{branch}")
	public ResponseDTO<SaleDTO> obtenerVentasUsuario(@PathVariable("id") Long id_client, @PathVariable("branch") Long id_branch) {
		return saleServiceImpl.findAllByIdAndByBranchId(id_client, id_branch);
	}
}