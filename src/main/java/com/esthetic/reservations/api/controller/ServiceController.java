package com.esthetic.reservations.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.MinService;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.ServiceDTO;
import com.esthetic.reservations.api.service.impl.SeriviceServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/owner/servicios")
public class ServiceController {
	
	@Autowired
	SeriviceServiceImpl seriviceServiceImpl;
	
	@GetMapping("/{id}")
	public ResponseEntity<ServiceDTO> obtenerServicio(@PathVariable Long id) {
		return new ResponseEntity<ServiceDTO>(seriviceServiceImpl.findById(id), HttpStatus.OK);
	}
	
	@GetMapping("/branch/{id}")
	public ResponseDTO<ServiceDTO> obtenerServicioBranch(@PathVariable Long id) {
		return seriviceServiceImpl.findAllIdBranch(id);
	}
	
	@GetMapping("/all")
	public ResponseDTO<ServiceDTO> obtenerTodosServicios(
			@RequestParam(value = "pageNum", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
		return seriviceServiceImpl.findAll(pageNumber, pageSize, sortBy, sortDir);
	}
	
	@PostMapping("/agregar")
	public ResponseEntity<ServiceDTO> agregarServicio(@RequestBody MinService servicio) {
		return new ResponseEntity<ServiceDTO>(seriviceServiceImpl.save(servicio), HttpStatus.CREATED);
	}
	
	@PutMapping("/actualizar/{id}")
	public ResponseEntity<ServiceDTO> actualizarServicio(@RequestBody MinService servicio, @PathVariable("id") Long id) {
		return new ResponseEntity<ServiceDTO>(seriviceServiceImpl.update(servicio, id), HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> deleteServicio(@PathVariable("id") Long id) {
		Map<String, String> response = new HashMap<String, String>();
		seriviceServiceImpl.delete(id);
		response.put("message", "Eliminado Correctamente");
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
	
}
