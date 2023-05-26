package com.esthetic.reservations.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.esthetic.reservations.api.dto.InventoryDTO;
import com.esthetic.reservations.api.dto.MinInventory;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.service.impl.InventoryServiceImpl;

@RestController
@RequestMapping("/api/owner/inventario")
public class InventoryController {
	@Autowired
	InventoryServiceImpl inventoryServiceImpl;
	
	@GetMapping("/{id}")
	public ResponseEntity<InventoryDTO> obtener(@PathVariable("id") Long id) {
		return new ResponseEntity<InventoryDTO>(inventoryServiceImpl.findById(id), HttpStatus.OK);
	}
	
	@GetMapping("/branch/{id}")
	public ResponseDTO<InventoryDTO> obtenerInventarioSucursal(@PathVariable("id") Long id) {
		return inventoryServiceImpl.findAllByBranchId(id);
	}
	
	@PostMapping(value = "/agregar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> agregar(@ModelAttribute MinInventory inventario, @RequestParam("file") MultipartFile file) {
		return new ResponseEntity<InventoryDTO>(inventoryServiceImpl.save(inventario, file), HttpStatus.ACCEPTED);
	}	
	
	@PutMapping(value = "/actualizar/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InventoryDTO> actualizar(@ModelAttribute MinInventory inventario, @RequestParam(value = "file", required = false) MultipartFile file, @PathVariable("id") Long id) {
		return new ResponseEntity<InventoryDTO>(inventoryServiceImpl.update(inventario, file, id), HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
		Map<String, String> response = new HashMap<String, String>();
		inventoryServiceImpl.eliminar(id);
		response.put("message", "Eliminado Correctamente");
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}	
