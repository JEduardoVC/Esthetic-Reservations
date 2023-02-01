package com.esthetic.reservations.api.service.impl;

import java.sql.Date;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.esthetic.reservations.api.dto.AppointmentDTO;
import com.esthetic.reservations.api.dto.MinAppointmentDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Service;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.AppointmentRepository;
import com.esthetic.reservations.api.service.AppointmentService;

@org.springframework.stereotype.Service
public class AppointmentServiceImpl extends GenericServiceImpl<Appointment, AppointmentDTO>
		implements AppointmentService {
	
	private AppointmentRepository appointmentRepository;
	private UserServiceImpl userServiceImpl;
	private SeriviceServiceImpl serviceImpl;
	private BranchServiceImpl branchServiceImpl;

	public AppointmentServiceImpl(AppointmentRepository repository, ModelMapper modelMapper) {
		super(repository, modelMapper, Appointment.class, AppointmentDTO.class);
	}
	
	public ResponseDTO<AppointmentDTO> findAllByIdBranch(Long id) {
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(id));
		ResponseDTO<AppointmentDTO> response = new ResponseDTO<>();
		//response.setContent(appointmentRepository.findAllByIdBranch(sucursal));
		return response;
	}
	
	public String save(@ModelAttribute MinAppointmentDTO cita) {
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(cita.getId_client()));
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(cita.getId_branch()));
		Date date_created = new Date(0);
		Date appointment_date = new Date(cita.getAppointment_Date().getDate());
		for(Long id_service : cita.getId_service()) {
			Service servicio = serviceImpl.mapToModel(serviceImpl.findById(id_service));
			Appointment appointment = new Appointment(date_created, appointment_date, usuario, null, servicio, null, sucursal);
			appointmentRepository.save(appointment);
		}
		return "Cita Registrada Exitosamente";
	}
	
	public String update(@ModelAttribute MinAppointmentDTO cita, Long id) {
		Appointment citaActual = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id", "no existe"));
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(cita.getId_branch()));
		UserEntity usuario = userServiceImpl.mapToModel(userServiceImpl.findById(cita.getId_client()));
		Date date_created = new Date(0);
		Date appointment_date = new Date(cita.getAppointment_Date().getDate());
		for(Long id_service : cita.getId_service()) {
			Service servicio = serviceImpl.mapToModel(serviceImpl.findById(id_service));
			Appointment appointment = new Appointment(citaActual.getId(), date_created, appointment_date, usuario, null, servicio, null, sucursal);
			appointmentRepository.save(appointment);
		}
		return "Cita Actualizada Correctamente";
	}
}