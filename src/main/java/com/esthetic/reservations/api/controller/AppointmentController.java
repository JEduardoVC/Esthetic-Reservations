package com.esthetic.reservations.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.esthetic.reservations.api.dto.AppointmentDTO;
import com.esthetic.reservations.api.dto.MinAppointmentDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.UserEntityDTO;
import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.AppointmentRepository;
import com.esthetic.reservations.api.service.MailService;
import com.esthetic.reservations.api.service.impl.AppointmentServiceImpl;
import com.esthetic.reservations.api.service.impl.BranchServiceImpl;
import com.esthetic.reservations.api.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
	
	@Autowired
	AppointmentServiceImpl appointmentServiceImpl;
	@Autowired
	UserServiceImpl serviceImpl;
	@Autowired
	BranchServiceImpl branchServiceImpl;
	@Autowired
    MailService mailService;
	
	@GetMapping("/{id}")
	public ResponseEntity<AppointmentDTO> obtenerCita(@PathVariable("id") Long id){
		return new ResponseEntity<AppointmentDTO>(appointmentServiceImpl.getId(id), HttpStatus.OK);
	}
	
	@GetMapping("/sucursal/{id}")
	public ResponseDTO<AppointmentDTO> obtenerCitaSucursal(@PathVariable("id") Long id_branch) {
		return appointmentServiceImpl.findAllByIdBranch(id_branch);
	}
	
	@PostMapping("/guardar")
	public ResponseEntity<AppointmentDTO> guardarCita(@RequestBody MinAppointmentDTO cita) {
		return new ResponseEntity<>(appointmentServiceImpl.save(cita), HttpStatus.CREATED);
	}
	
	@PutMapping("/actualizar/{id}")
	public ResponseEntity<String> actualizarCita(@RequestBody MinAppointmentDTO cita, @PathVariable("id") Long id) {
		return new ResponseEntity<String>(appointmentServiceImpl.update(cita, id), HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<String> eliminarCita(@PathVariable("id") Long id) {
		return new ResponseEntity<String>(appointmentServiceImpl.eliminar(id), HttpStatus.ACCEPTED);
	}
	
    @PostMapping(value = "/sendMultiMail", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sendMultiMail(@RequestParam("mail") Long mail, @RequestParam("qr") MultipartFile qr, @RequestParam("branch") Long id, @RequestParam("appointment") Long id_cita) {
    	return new ResponseEntity<Object>(appointmentServiceImpl.sendMail(mail, qr, id, id_cita), HttpStatus.ACCEPTED);
    }
}
