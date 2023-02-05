package com.esthetic.reservations.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esthetic.reservations.api.dto.AppointmentDTO;
import com.esthetic.reservations.api.dto.MinAppointmentDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.service.impl.AppointmentServiceImpl;
import com.esthetic.reservations.api.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
	
	@Autowired
	AppointmentServiceImpl appointmentServiceImpl;
	@Autowired
	UserServiceImpl serviceImpl;
	
	@GetMapping("/{id}")
	public ResponseEntity<AppointmentDTO> obtenerCita(@PathVariable("id") Long id){
		return new ResponseEntity<AppointmentDTO>(appointmentServiceImpl.getId(id), HttpStatus.OK);
	}
	
	@GetMapping("/sucursal/{id}")
	public ResponseDTO<AppointmentDTO> obtenerCitaSucursal(@PathVariable("id") Long id_branch) {
		return appointmentServiceImpl.findAllByIdBranch(id_branch);
	}
	
	@PostMapping("/guardar")
	public ResponseEntity<String> guardarCita(@RequestBody MinAppointmentDTO cita) {
		return new ResponseEntity<String>(appointmentServiceImpl.save(cita), HttpStatus.CREATED);
	}
	
	@PutMapping("/actualizar/{id}")
	public ResponseEntity<String> actualizarCita(@RequestBody MinAppointmentDTO cita, @PathVariable("id") Long id) {
		return new ResponseEntity<String>(appointmentServiceImpl.update(cita, id), HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<String> eliminarCita(@PathVariable("id") Long id) {
		appointmentServiceImpl.delete(id);
		return new ResponseEntity<String>("Cita Eliminada Correctamente", HttpStatus.ACCEPTED);
	}
}
