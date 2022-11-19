package com.esthetic.reservations.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.ServiceDTO;
import com.esthetic.reservations.api.repository.ServiceRepository;
import com.esthetic.reservations.api.service.impl.BranchServiceImpl;
import com.esthetic.reservations.api.service.impl.SeriviceServiceImpl;
import com.esthetic.reservations.api.util.AppConstants;

@RestController
@RequestMapping("/api/admin/servicios")
public class ServiciosController {
	
	@Autowired
	SeriviceServiceImpl seriviceServiceImpl;
	
	@Autowired
	BranchServiceImpl branchServiceImpl;
	
	@GetMapping("/{id}")
	public ResponseEntity<ServiceDTO> obtenerServicio(@RequestParam Long id) {
		return new ResponseEntity<ServiceDTO>(seriviceServiceImpl.findById(id), HttpStatus.OK);
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
	public ResponseEntity<ServiceDTO> agregarServicio(@RequestBody ServiceDTO servicio) {
		return new ResponseEntity<ServiceDTO>(seriviceServiceImpl.save(servicio), HttpStatus.CREATED);
	}
	
	@PutMapping("/actualizar")
	public ResponseEntity<ServiceDTO> actualizarServicio(@RequestBody ServiceDTO servicio) {
		return new ResponseEntity<ServiceDTO>(seriviceServiceImpl.update(servicio, servicio.getId()), HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/eliminar")
	public ResponseEntity<String> deleteServicio(@RequestParam Long id) {
		seriviceServiceImpl.delete(id);
		return new ResponseEntity<String>("Eliminado Correctamente", HttpStatus.ACCEPTED);
	}
	
}
